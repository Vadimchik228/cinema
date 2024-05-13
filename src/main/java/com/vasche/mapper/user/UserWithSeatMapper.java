package com.vasche.mapper.user;

import com.vasche.dto.user.UserWithSeatDto;
import com.vasche.entity.User;
import com.vasche.mapper.Mapper;

public class UserWithSeatMapper implements Mapper<User, UserWithSeatDto> {

    public UserWithSeatMapper() {
    }

    @Override
    public UserWithSeatDto mapFrom(User user) {
        return null;
    }

    public UserWithSeatDto mapFrom(User user, Integer seatNumber, Integer reservationId) {
        return UserWithSeatDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .seatNumber(seatNumber)
                .reservationId(reservationId)
                .build();
    }

}
