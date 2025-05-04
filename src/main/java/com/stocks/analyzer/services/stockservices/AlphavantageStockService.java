package com.stocks.analyzer.services.stockservices;

import com.stocks.analyzer.constants.Symbols;
import com.stocks.analyzer.exceptions.NoSuchSymbolException;
import com.stocks.analyzer.models.Candle;
import com.stocks.analyzer.models.CandleCollection;
import com.stocks.analyzer.models.Stock;
import com.stocks.analyzer.models.alphavantagemodels.AlphavantageStockResponse;
import com.stocks.analyzer.models.controllermodels.StockRequest;
import com.stocks.analyzer.repositoryservices.HistoricalDataRepositoryService;
import com.stocks.analyzer.services.externalfetchapi.ExternalFetchApi;
import com.stocks.analyzer.utils.StockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlphavantageStockService implements StockService {

    private static final String DAILY = "DAILY";
    private static final String MONTHLY = "MONTHLY";

    private static final String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    private static final String TIME_SERIES_MONTHLY = "TIME_SERIES_MONTHLY";

    @Autowired
    private ExternalFetchApi<AlphavantageStockResponse> alphavantageService;

    @Autowired
    private HistoricalDataRepositoryService repositoryService;

    public Stock dummyStock(String symbolName) {
        Symbols symbol = Symbols.getSymbol(symbolName);
        return Stock.builder()
                .stockId("123")
                .symbol(symbol)
                .companyName(Symbols.getCompanyName(symbol))
                .open(100.00)
                .close(200.00)
                .low(50.00)
                .high(250.00)
                .volume(10000L)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CandleCollection getDailyStocks(StockRequest request) throws NoSuchSymbolException {
        Symbols symbol = Symbols.getSymbolByName(request.getSymbol());
        if (symbol == Symbols.UNSPECIFIED) {
            throw new NoSuchSymbolException("No such SYMBOL as " + request.getSymbol());
        }

        String function = resolveFunction(request.getFunction());
        LocalDateTime toDateTime = request.getTo() != null ? request.getTo() : LocalDateTime.now();
        LocalDateTime fromDateTime = request.getFrom() != null ? request.getFrom() : toDateTime.minusDays(30L);


        // Check if database already has historical data of symbol.
        Optional<CandleCollection> historicalStockCollectionDBOptional = repositoryService.getAllStockBySymbol(symbol, fromDateTime, toDateTime);
        CandleCollection historicalCandleCollection = historicalStockCollectionDBOptional.orElseGet(CandleCollection::new);

        // Check if today's date is equal to latest date from db
        // If true return db value
        // Else, check date how many missing, take account of holidays, add missing data to db and return latest list.
        if (!historicalCandleCollection.isEmpty()) {
            LocalDateTime latestTimeFromDb = historicalCandleCollection.getCandles().getFirst().getDateTime();
            LocalDateTime marketToday = getLatestMarketDay();

            if (!marketToday.isEqual(latestTimeFromDb)) {
                CandleCollection missingCandles = fetchAndFilterLatestCandles(symbol, function, latestTimeFromDb);
                saveInDB(missingCandles);
                historicalCandleCollection.addCandles(missingCandles.getCandles());
            }
            return historicalCandleCollection;
        }

        CandleCollection candleCollection = fetchFullSeries(symbol, function);
        saveInDB(candleCollection);

        candleCollection.filterBetween(fromDateTime, toDateTime);
        candleCollection.reverseCollection();

        return candleCollection;
    }

    private String resolveFunction(String function) {

        return switch (function) {
            case DAILY -> TIME_SERIES_DAILY;
            case MONTHLY -> TIME_SERIES_MONTHLY;
            default -> function;
        };
    }

    /**
     * Saves in DB
     *
     * @param candleCollection collection of historical data.
     */
    private void saveInDB(CandleCollection candleCollection) {
        boolean isSaveSuccessful = repositoryService.saveAllHistoricalData(candleCollection);

        if (!isSaveSuccessful) {
            System.out.println("DB save UNSUCCESSFUL!");
        }
    }

    /**
     * Gets the latest market date
     *
     * @return latest localdatetime
     */
    private LocalDateTime getLatestMarketDay() {
        LocalDate date = LocalDate.now();
        return switch (date.getDayOfWeek()) {
            case SATURDAY -> date.minusDays(1).atStartOfDay();
            case SUNDAY -> date.minusDays(2).atStartOfDay();
            default -> date.atStartOfDay();
        };
    }

    /**
     * Fetches full series from Alphavantage API
     *
     * @param symbol   <code>Symbols</code> type
     * @param function Series
     * @return <code>CandleCollection</code>
     */
    private CandleCollection fetchFullSeries(Symbols symbol, String function) {
        AlphavantageStockResponse response = alphavantageService.fetch(symbol, function, null, null); // from/to irrelevant
        return StockUtil.convertAlphavantageStockToStocks(response);
    }

    /**
     * Fetches and filers candles after given datetime.
     *
     * @param symbol    <code>Symbols</code> type
     * @param function  Series
     * @param afterDate Required to filter candles
     * @return <code>CandleCollection</code> after <code>afterdate</code> parameter.
     */
    private CandleCollection fetchAndFilterLatestCandles(Symbols symbol, String function, LocalDateTime afterDate) {
        CandleCollection fullSeries = fetchFullSeries(symbol, function);
        List<Candle> newCandles = fullSeries.getCandles().stream()
                .filter(c -> c.getDateTime().isAfter(afterDate))
                .sorted(CandleCollection.candleComparator()) // Sorts in ascending order
                .collect(Collectors.toList());

        CandleCollection latestOnly = new CandleCollection();
        latestOnly.addCandles(newCandles);
        return latestOnly;
    }
}
