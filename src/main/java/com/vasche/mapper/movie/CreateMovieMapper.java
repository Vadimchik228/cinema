package com.vasche.mapper.movie;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.mapper.Mapper;

public class CreateMovieMapper implements Mapper<CreateMovieDto, Movie> {
    private static final String IMAGE_FOLDER = "\\movies";

    public CreateMovieMapper() {
    }

    @Override
    public Movie mapFrom(final CreateMovieDto createMovieDto) {
        return Movie.builder()
                .id(createMovieDto.getId() == null ? 1 : Integer.parseInt(createMovieDto.getId()))
                .title(createMovieDto.getTitle())
                .description(createMovieDto.getDescription())
                .minimumAge(Integer.parseInt(createMovieDto.getMinimumAge()))
                .durationMin(Integer.parseInt(createMovieDto.getDurationMin()))
                .genre(Genre.valueOf(createMovieDto.getGenre()))
                .imageUrl(IMAGE_FOLDER + createMovieDto.getImage().getSubmittedFileName())
                .build();
    }

}

