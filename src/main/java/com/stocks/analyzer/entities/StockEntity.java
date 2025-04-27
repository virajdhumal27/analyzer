package com.stocks.analyzer.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {

    @Id
    private String symbol;

    @Column(name = "company_name")
    private String companyName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
