package com.vasche.mapper.movie;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.mapper.Mapper;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateMovieMapper implements Mapper<CreateMovieDto, Movie> {
    private static final CreateMovieMapper INSTANCE = new CreateMovieMapper();

    private static final String IMAGE_FOLDER = "\\movies";

    public static CreateMovieMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Movie mapFrom(final CreateMovieDto createMovieDto) {
        return Movie.builder()
                .title(createMovieDto.getTitle())
                .description(createMovieDto.getDescription())
                .minimumAge(Integer.valueOf(createMovieDto.getMinimumAge()))
                .durationMin(Integer.valueOf(createMovieDto.getDurationMin()))
                .genre(Genre.valueOf(createMovieDto.getGenre()))
                .imageUrl(createMovieDto.getImage() != null ?
                        IMAGE_FOLDER + createMovieDto.getImage().getSubmittedFileName()
                        : null)
                .build();
    }

}

