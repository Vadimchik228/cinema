package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningWithHallDto;
import com.vasche.entity.Hall;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;

public class ScreeningWithHallMapper implements Mapper<Screening, ScreeningWithHallDto> {
    public ScreeningWithHallMapper() {
    }

    @Override
    public ScreeningWithHallDto mapFrom(final Screening screening) {
        return null;
    }

    public ScreeningWithHallDto mapFrom(final Screening screening, final Hall hall) {
        return ScreeningWithHallDto.builder()
                .id(screening.getId())
                .price(screening.getPrice())
                .startTime(String.valueOf(screening.getStartTime()).replace("T", " "))
                .movieId(screening.getMovieId())
                .hallId(screening.getHallId())
                .hallName(hall.getName())
                .build();
    }
}