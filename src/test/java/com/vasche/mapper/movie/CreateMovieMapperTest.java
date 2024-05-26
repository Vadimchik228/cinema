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
                .id("1")
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .image(null)
                .build();

        Movie actualResult = mapper.mapFrom(dto);

        Movie expectedResult = Movie.builder()
                .id(1)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .imageUrl(null)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
