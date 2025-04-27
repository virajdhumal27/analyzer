package com.stocks.analyzer.repositories;

import com.stocks.analyzer.entities.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, String> {

    List<HistoricalDataEntity> findAllBySymbol(String symbol);

    @Query(value = "SELECT hd FROM HistoricalDataEntity hd WHERE hd.symbol = :symbol AND hd.dateTime BETWEEN :from AND :to")
    List<HistoricalDataEntity> findAllBySymbolBetweenFromAndTo(@Param("symbol") String symbol, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
