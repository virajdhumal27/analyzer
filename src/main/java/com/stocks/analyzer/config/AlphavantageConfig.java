package com.stocks.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alphavantage")
public class AlphavantageConfig {
    private String apiKey;
    private String baseUrl;
}
