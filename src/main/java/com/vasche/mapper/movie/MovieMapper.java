package com.vasche.mapper.movie;

import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovieMapper implements Mapper<Movie, MovieDto> {
    private static final MovieMapper INSTANCE = new MovieMapper();

    public static MovieMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public MovieDto mapFrom(final Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMin(movie.getDurationMin())
                .minimumAge(movie.getMinimumAge())
                .genre(movie.getGenre())
                .imageUrl(movie.getImageUrl())
                .build();
    }
}
