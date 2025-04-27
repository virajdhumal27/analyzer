package com.stocks.analyzer.exchanges;

import com.stocks.analyzer.models.CandleCollection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockResponseBody {
    private String status;
    private CandleCollection candleCollection;
}
