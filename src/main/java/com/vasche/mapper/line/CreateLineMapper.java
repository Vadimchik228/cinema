package com.vasche.mapper.line;

import com.vasche.dto.line.CreateLineDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Line;
import com.vasche.entity.Screening;
import com.vasche.mapper.Mapper;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateLineMapper implements Mapper<CreateLineDto, Line> {
    private static final CreateLineMapper INSTANCE = new CreateLineMapper();

    public static CreateLineMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Line mapFrom(final CreateLineDto createLineDto) {
        return Line.builder()
                .number(Integer.valueOf(createLineDto.getNumber()))
                .hallId(Integer.valueOf(createLineDto.getHallId()))
                .build();
    }

}
