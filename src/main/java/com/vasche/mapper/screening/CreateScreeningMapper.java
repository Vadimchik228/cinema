package com.vasche.mapper.screening;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateScreeningMapper implements Mapper<CreateScreeningDto, Screening> {
    private static final String PATTERN = "yyyy-MM-dd HH:mm";

    public CreateScreeningMapper() {
    }

    @Override
    public Screening mapFrom(final CreateScreeningDto createScreeningDto) {
        return Screening.builder()
                .movieId(Integer.parseInt(createScreeningDto.getMovieId()))
                .price(new BigDecimal(createScreeningDto.getPrice()))
                .startTime(LocalDateTime.parse(createScreeningDto.getStartTime(), DateTimeFormatter.ofPattern(PATTERN)))
                .hallId(Integer.parseInt(createScreeningDto.getHallId()))
                .build();
    }

}