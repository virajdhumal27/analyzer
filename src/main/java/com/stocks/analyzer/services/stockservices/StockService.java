package com.stocks.analyzer.services.stockservices;

import com.stocks.analyzer.exceptions.NoSuchSymbolException;
import com.stocks.analyzer.models.CandleCollection;
import com.stocks.analyzer.models.controllermodels.StockRequest;

public interface StockService {

    /**
     * @param request <code>StockRequest</code> type
     * @return <code>CandleCollection</code> in decreasing order of dates.
     * @throws NoSuchSymbolException thrown when Symbol passed is invalid.
     */
    CandleCollection getDailyStocks(StockRequest request) throws NoSuchSymbolException;
}
