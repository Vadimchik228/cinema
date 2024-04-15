package com.vasche.dto.filter;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MovieFilterDto {
    String title;
    Genre genre;
    Integer minimumAge;
}
