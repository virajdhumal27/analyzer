package com.stocks.analyzer.repositories;

import com.stocks.analyzer.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String> {
//    Optional<StockEntity> findByCompanyName(String companyName);
//    Optional<StockEntity> findBySymbol(String symbol);
}
