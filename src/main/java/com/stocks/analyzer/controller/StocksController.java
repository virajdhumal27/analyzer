package com.stocks.analyzer.controller;


import com.stocks.analyzer.exceptions.NoSuchSymbolException;
import com.stocks.analyzer.exchanges.StockRequestBody;
import com.stocks.analyzer.exchanges.StockResponseBody;
import com.stocks.analyzer.models.CandleCollection;
import com.stocks.analyzer.models.controllermodels.StockRequest;
import com.stocks.analyzer.services.stockservices.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(StocksController.ENDPOINT + StocksController.VERSION)
public class StocksController {
    public static final String ENDPOINT = "/analyzer";
    public static final String VERSION = "/v1";
    public static final String GET_STOCKS = "/stocks";

    @Autowired
    private StockService stockService;

    @GetMapping(GET_STOCKS)
    public ResponseEntity<StockResponseBody> getCandles(@RequestBody StockRequestBody request) {
        StockRequest stockRequest = new StockRequest(
                request.getFunction(),
                request.getSymbol(),
                request.getFrom(),
                request.getTo()
        );

        StockResponseBody response = new StockResponseBody();
        try {
            CandleCollection candleCollection = stockService.getDailyStocks(stockRequest);
            response.setCandleCollection(candleCollection);
            response.setStatus("OK");
        } catch (NoSuchSymbolException e) {
            response.setStatus("No such symbol present");
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok().body(response);
    }
}
