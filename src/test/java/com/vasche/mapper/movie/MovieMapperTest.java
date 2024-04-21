package com.vasche.mapper.movie;

import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieMapperTest {
    private final MovieMapper mapper = new MovieMapper();

    @Test
    void map() {
        Movie movie = Movie.builder()
                .id(1)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();

        MovieDto actualResult = mapper.mapFrom(movie);

        MovieDto expectedResult = MovieDto.builder()
                .id(1)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
