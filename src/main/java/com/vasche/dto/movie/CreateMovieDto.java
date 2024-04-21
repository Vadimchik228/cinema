package com.vasche.dto.movie;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CreateMovieDto {
    private final String title;
    private final String description;
    private final String durationMin;
    private final String minimumAge;
    private final String genre;
    private final Part image;
}
