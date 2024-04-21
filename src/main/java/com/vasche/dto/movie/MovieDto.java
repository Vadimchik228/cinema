package com.vasche.dto.movie;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class MovieDto {
    private final int id;
    private final String title;
    private final String description;
    private final Integer durationMin;
    private final Integer minimumAge;
    private final String imageUrl;
    private final Genre genre;
}
