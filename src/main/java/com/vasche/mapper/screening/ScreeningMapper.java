package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScreeningMapper implements Mapper<Screening, ScreeningDto> {
    private static final ScreeningMapper INSTANCE = new ScreeningMapper();

    public static ScreeningMapper getInstance() {
        return INSTANCE;
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
