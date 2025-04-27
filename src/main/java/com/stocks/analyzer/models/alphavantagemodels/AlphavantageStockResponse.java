package com.stocks.analyzer.models.alphavantagemodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class AlphavantageStockResponse {

    @JsonProperty("Meta Data")
    private MetaData metaData;

    @JsonProperty("Time Series (Daily)")
    private Map<LocalDate, DailyData> timeSeries;

    public List<DailyData> getDailyData() {
        return timeSeries.values().stream().toList();
    }
}