package com.vasche.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screening {
    private int id;
    private LocalDateTime startTime;
    private BigDecimal price;
    private int movieId;
    private int hallId;
}