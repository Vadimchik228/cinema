package com.vasche.mapper.hall;

import com.vasche.dto.hall.CreateHallDto;
import com.vasche.entity.Hall;
import com.vasche.mapper.Mapper;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateHallMapper implements Mapper<CreateHallDto, Hall> {
    private static final CreateHallMapper INSTANCE = new CreateHallMapper();

    public static CreateHallMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Hall mapFrom(final CreateHallDto createHallDto) {
        return Hall.builder()
                .name(createHallDto.getName())
                .build();
    }

}