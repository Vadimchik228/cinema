package com.vasche.mapper.movie;

import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.mapper.Mapper;


public class MovieMapper implements Mapper<Movie, MovieDto> {

    public MovieMapper() {
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
