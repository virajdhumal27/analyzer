package com.stocks.analyzer.models.controllermodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockRequest {
    private String function;
    private String symbol;
    private LocalDateTime from;
    private LocalDateTime to;
}
