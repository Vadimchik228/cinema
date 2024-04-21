package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;


public class ScreeningMapper implements Mapper<Screening, ScreeningDto> {

    public ScreeningMapper() {
    }

    @Override
    public ScreeningDto mapFrom(final Screening screening) {
        return ScreeningDto.builder()
                .id(screening.getId())
                .startTime(screening.getStartTime())
                .movieId(screening.getMovieId())
                .price(screening.getPrice())
                .hallId(screening.getHallId())
                .build();
    }
}
