package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningAllDataDto;
import com.vasche.entity.Hall;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;

public class ScreeningAllDataMapper implements Mapper<Screening, ScreeningAllDataDto> {

    public ScreeningAllDataMapper() {
    }

    @Override
    public ScreeningAllDataDto mapFrom(final Screening screening) {
        return null;
    }

    public ScreeningAllDataDto mapFrom(final Screening screening, final Movie movie, final Hall hall) {
        return ScreeningAllDataDto.builder()
                .id(screening.getId())
                .price(screening.getPrice())
                .startTime(String.valueOf(screening.getStartTime()).replace("T", " "))
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMin(movie.getDurationMin())
                .genre(movie.getGenre())
                .minimumAge(movie.getMinimumAge())
                .imageUrl(movie.getImageUrl())
                .hallName(hall.getName())
                .build();
    }
}
