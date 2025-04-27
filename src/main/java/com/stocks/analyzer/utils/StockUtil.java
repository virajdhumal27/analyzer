package com.stocks.analyzer.utils;

import com.stocks.analyzer.constants.Symbols;
import com.stocks.analyzer.models.Stock;
import com.stocks.analyzer.models.CandleCollection;
import com.stocks.analyzer.models.alphavantagemodels.AlphavantageStockResponse;
import com.stocks.analyzer.models.alphavantagemodels.DailyData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class StockUtil {

    /**
     * Util method to convert response from Alphavantage to StockCollection.
     * @param alphavantageStockResponse
     * @return <code>StockCollection</code>
     */
    public static CandleCollection convertAlphavantageStockToStocks(AlphavantageStockResponse alphavantageStockResponse) {

        Symbols symbol = Symbols.getSymbolByName(alphavantageStockResponse.getMetaData().getSymbol());

        CandleCollection candleCollection = new CandleCollection();

        for (Map.Entry<LocalDate, DailyData> entries: alphavantageStockResponse.getTimeSeries().entrySet()) {
            LocalDate localDate = entries.getKey();
            DailyData dailyData = entries.getValue();

            Stock.StockBuilder stockBuilder = Stock.builder();

            stockBuilder.dateTime(LocalDateTime.of(localDate, LocalTime.MIDNIGHT));
            stockBuilder.stockId(UUID.randomUUID().toString());
            stockBuilder.symbol(symbol);
            stockBuilder.companyName(Symbols.getCompanyName(symbol));

            stockBuilder.open(Double.valueOf(dailyData.getOpen()));
            stockBuilder.close(Double.valueOf(dailyData.getClose()));
            stockBuilder.high(Double.valueOf(dailyData.getHigh()));
            stockBuilder.low(Double.valueOf(dailyData.getLow()));
            stockBuilder.volume(Long.valueOf(dailyData.getVolume()));

            Stock stock = stockBuilder.build();
            candleCollection.addCandle(stock);
        }

        return candleCollection;
    }
}
