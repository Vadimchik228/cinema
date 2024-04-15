package com.vasche.mapper.screening;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateScreeningMapper implements Mapper<CreateScreeningDto, Screening> {
    private static final CreateScreeningMapper INSTANCE = new CreateScreeningMapper();

    private static final String PATTERN = "yyyy-MM-dd HH:mm";

    public static CreateScreeningMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Screening mapFrom(final CreateScreeningDto createScreeningDto) {
        return Screening.builder()
                .movieId(Integer.valueOf(createScreeningDto.getMovieId()))
                .price(new BigDecimal(createScreeningDto.getPrice()))
                .startTime(LocalDateTime.parse(createScreeningDto.getStartTime(), DateTimeFormatter.ofPattern(PATTERN)))
                .hallId(Integer.valueOf(createScreeningDto.getHallId()))
                .build();
    }

}