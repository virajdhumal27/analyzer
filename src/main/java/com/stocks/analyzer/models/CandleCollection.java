package com.stocks.analyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CandleCollection {
    private List<Candle> candles = new ArrayList<>();

    public void addCandle(Candle candle) {
        this.candles.add(candle);
    }

    public void addCandles(List<Candle> newCandles) {
        this.candles.addAll(newCandles);
    }

    public void addCandlesInFront(List<Candle> newCandles) {
        this.candles.addAll(0, newCandles);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.candles.isEmpty();
    }

    /**
     * Modifies the list and filters all the candles between <code>from</code> and <code>to</code>.
     * If <code>from</code> is greater than <code>to</code> then the list is unaffected.
     *
     * @param from <code>LocalDateTime</code> source
     * @param to   <code>LocalDateTime</code> end
     */
    @JsonIgnore
    public void filterBetween(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) return;

        this.candles = this.candles.stream()
                .filter(c -> !c.getDateTime().isBefore(from) && !c.getDateTime().isAfter(to))
                .collect(Collectors.toList());
    }

    /**
     * Reverses the candleCollection in-place.
     */
    @JsonIgnore
    public void reverseCollection() {
        this.candles = this.candles.reversed();
    }

    /**
     * Comparator to sort candles according to datetime in ASCENDING order.
     *
     * @return <code>Comparator<Candle></code>
     */
    @JsonIgnore
    public static Comparator<Candle> candleComparator() {
        return (candle1, candle2) -> {
            if (candle1.getDateTime().isBefore(candle2.getDateTime())) return -1;
            if (candle1.getDateTime().isAfter(candle2.getDateTime())) return 1;
            return 0;
        };
    }
}
