package com.vasche.dto.movie;

import com.vasche.entity.Genre;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateMovieDto {
    String title;
    String description;
    String durationMin;
    String minimumAge;
    String genre;
    Part image;
}
