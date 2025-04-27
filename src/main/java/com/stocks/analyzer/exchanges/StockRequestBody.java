package com.stocks.analyzer.exchanges;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StockRequestBody {
    private String function;
    private String symbol;
    private LocalDateTime from;
    private LocalDateTime to;
}
