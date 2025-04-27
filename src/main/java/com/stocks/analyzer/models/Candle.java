package com.stocks.analyzer.models;

import com.stocks.analyzer.constants.Symbols;

import java.time.LocalDateTime;

public interface Candle {

    String getStockId();
    Symbols getSymbol();
    String getCompanyName();
    Double getOpen();
    Double getClose();
    Double getHigh();
    Double getLow();
    Long getVolume();
    LocalDateTime getDateTime();

}
