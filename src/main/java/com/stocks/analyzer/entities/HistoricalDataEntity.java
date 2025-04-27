package com.stocks.analyzer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historical_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String id;

    private String symbol;

    @Column
    private Double open;

    @Column
    private Double close;

    @Column
    private Double high;

    @Column
    private Double low;

    @Column
    private Long volume;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
