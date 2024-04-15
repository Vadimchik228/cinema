package com.vasche.mapper.hall;

import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HallMapper implements Mapper<Hall, HallDto> {
    private static final HallMapper INSTANCE = new HallMapper();

    public static HallMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public HallDto mapFrom(Hall hall) {
        return HallDto.builder()
                .id(hall.getId())
                .name(hall.getName())
                .build();
    }
}
