package com.stocks.analyzer.services.externalfetchapi;

import com.stocks.analyzer.constants.Symbols;

import java.time.LocalDateTime;

public interface ExternalFetchApi<T> {

    T fetch(Symbols symbol);

    T fetch(Symbols symbol, String function, LocalDateTime from, LocalDateTime to);

}
