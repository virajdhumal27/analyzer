package com.stocks.analyzer.services.externalfetchapi;

import com.stocks.analyzer.config.AlphavantageConfig;
import com.stocks.analyzer.constants.Symbols;
import com.stocks.analyzer.models.alphavantagemodels.AlphavantageStockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AlphavantageFetchService implements ExternalFetchApi<AlphavantageStockResponse> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private AlphavantageConfig alphavantageConfig;

    @Override
    public AlphavantageStockResponse fetch(Symbols symbol) {

        String FUNCTION = "TIME_SERIES_DAILY";

        WebClient webClient = webClientBuilder
                .baseUrl(alphavantageConfig.getBaseUrl())
                .build();

        Mono<AlphavantageStockResponse> alphavantageStockMono = webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", FUNCTION)
                        .queryParam("symbol", symbol.toString())
                        .queryParam("apikey", alphavantageConfig.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(AlphavantageStockResponse.class);

        return alphavantageStockMono.block();
    }

    @Override
    public AlphavantageStockResponse fetch(Symbols symbol, String function, LocalDateTime from, LocalDateTime to) {
        WebClient webClient = webClientBuilder
                .baseUrl(alphavantageConfig.getBaseUrl())
                .build();

        Mono<AlphavantageStockResponse> alphavantageStockMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", function)
                        .queryParam("symbol", symbol.toString())
                        .queryParam("apikey", alphavantageConfig.getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(AlphavantageStockResponse.class);

        return alphavantageStockMono.block();
    }
}
