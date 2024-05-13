package com.vasche.mapper.screening;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;
import com.vasche.util.LocalDateTimeFormatter;

import java.math.BigDecimal;

public class CreateScreeningMapper implements Mapper<CreateScreeningDto, Screening> {

    public CreateScreeningMapper() {
    }

    @Override
    public Screening mapFrom(final CreateScreeningDto createScreeningDto) {
        return Screening.builder()
                .id(createScreeningDto.getId() == null ? 1 : Integer.parseInt(createScreeningDto.getId()))
                .movieId(Integer.parseInt(createScreeningDto.getMovieId()))
                .price(new BigDecimal(createScreeningDto.getPrice()))
                .startTime(LocalDateTimeFormatter.format(createScreeningDto.getStartTime()))
                .hallId(Integer.parseInt(createScreeningDto.getHallId()))
                .build();
    }

}