package com.vasche.dto.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class UserWithSeatDto {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int reservationId;
    private final int seatNumber;
}
