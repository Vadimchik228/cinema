package com.vasche.dto.line;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LineDto {
    Integer id;
    Integer number;
    Integer hallId;
}
