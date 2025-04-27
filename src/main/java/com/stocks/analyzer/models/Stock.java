package com.stocks.analyzer.models;

import com.stocks.analyzer.constants.Symbols;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Stock data should only be used for setting the data in initial phase.
 */
@Data
@Builder
public class Stock implements Candle {

    private String stockId;
    private Symbols symbol;
    private String companyName;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Long volume;
    private LocalDateTime dateTime;

}
