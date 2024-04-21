package com.vasche.mapper.movie;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateMovieMapperTest {
    private final CreateMovieMapper mapper = new CreateMovieMapper();

    @Test
    void map() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();

        Movie actualResult = mapper.mapFrom(dto);

        Movie expectedResult = Movie.builder()
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
