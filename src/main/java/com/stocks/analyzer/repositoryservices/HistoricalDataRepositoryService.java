package com.stocks.analyzer.repositoryservices;

import com.stocks.analyzer.constants.Symbols;
import com.stocks.analyzer.entities.HistoricalDataEntity;
import com.stocks.analyzer.models.Candle;
import com.stocks.analyzer.models.Stock;
import com.stocks.analyzer.models.CandleCollection;
import com.stocks.analyzer.repositories.HistoricalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HistoricalDataRepositoryService {

    @Autowired
    private HistoricalDataRepository repository;

    /**
     * Returns an <code>Optional</code> object of <code>CandleCollection</code> consisting of list of candles with descending order of DateTime.
     * @param symbol Stock symbol
     * @param from From starting date. If null, then 30 days before <code>to</code>
     * @param to To end date. If null, then uses today's date and time.
     * @return Optional object of CandleCollection ordered by latest.
     */
    public Optional<CandleCollection> getAllStockBySymbol(Symbols symbol, LocalDateTime from, LocalDateTime to) {
        List<HistoricalDataEntity> entities = repository.findAllBySymbolBetweenFromAndTo(symbol.toString(), from, to);

        if (entities.isEmpty()) {
            return Optional.empty();
        }
        CandleCollection candleCollection = new CandleCollection();

        for (HistoricalDataEntity entity: entities) {
            Stock stock = Stock.builder()
                    .symbol(symbol)
                    .stockId(entity.getId())
                    .companyName(Symbols.getCompanyName(symbol))
                    .open(entity.getOpen())
                    .close(entity.getClose())
                    .high(entity.getHigh())
                    .low(entity.getLow())
                    .volume(entity.getVolume())
                    .dateTime(entity.getDateTime())
                    .build();
            candleCollection.addCandle(stock);
        }

        // Order stocks by descending order by DateTime (means Latest DateTime first)
        Comparator<Candle> candleComparator = (o1, o2) -> {
            if (o1.getDateTime().isBefore(o2.getDateTime())) return 1;
            if (o1.getDateTime().isAfter(o2.getDateTime())) return -1;
            return 0;
        };

        candleCollection.getCandles().sort(candleComparator);

        return Optional.of(candleCollection);
    }

    public boolean saveAllHistoricalData(CandleCollection candleCollection) {
        List<HistoricalDataEntity> historicalDataEntities = convertCandleCollectionForDatabase(candleCollection);

        if (historicalDataEntities == null) {
            return false;
        }

        // Sort in ascending order
        historicalDataEntities.sort((h1, h2) -> {
            if(h1.getDateTime().isBefore(h2.getDateTime())) return -1;
            if(h1.getDateTime().isAfter(h2.getDateTime())) return 1;
            return 0;
        });
        repository.saveAll(historicalDataEntities);
        return true;
    }

    private static List<HistoricalDataEntity> convertCandleCollectionForDatabase(CandleCollection candleCollection) {
        List<Candle> candles = candleCollection.getCandles();
        if (candles != null && !candles.isEmpty()) {
            List<HistoricalDataEntity> entities = new ArrayList<>();
            for (Candle candle: candles) {
                HistoricalDataEntity historicalDataEntity = HistoricalDataEntity.builder()
                        .open(candle.getOpen())
                        .close(candle.getClose())
                        .low(candle.getLow())
                        .high(candle.getHigh())
                        .volume(candle.getVolume())
                        .symbol(candle.getSymbol().name())
                        .dateTime(candle.getDateTime())
                        .build();
                entities.add(historicalDataEntity);
            }

            return entities;
        }
        return null;
    }
}
