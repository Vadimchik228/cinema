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
                .title(createMovieDto.getTitle())
                .description(createMovieDto.getDescription())
                .minimumAge(Integer.parseInt(createMovieDto.getMinimumAge()))
                .durationMin(Integer.parseInt(createMovieDto.getDurationMin()))
                .genre(Genre.valueOf(createMovieDto.getGenre()))
                .imageUrl(createMovieDto.getImage() != null ?
                        IMAGE_FOLDER + createMovieDto.getImage().getSubmittedFileName()
                        : null)
                .build();
    }

}

