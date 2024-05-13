package com.vasche.dto.reservation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class ReservationAllDataDto {
    private final int id;

    private final int userId;
    private final String firstName;
    private final String lastName;
    private final String email;

    private final int screeningId;
    private final LocalDateTime startTime;
    private final BigDecimal price;
    private final String title;
    private final int durationMin;
    private final int minimumAge;

    private final String hallName;
    private final int seatId;
    private final int rowNumber;
    private final int seatNumber;
}
