package com.vasche.dto.movie;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MovieDto {
    Integer id;
    String title;
    String description;
    Integer durationMin;
    Integer minimumAge;
    String imageUrl;
    Genre genre;
}
