package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningAllDataDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Hall;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScreeningAllDataMapperTest {
    private final ScreeningAllDataMapper mapper = new ScreeningAllDataMapper();

    @Test
    void map() {
        Screening screening = Screening.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .movieId(1)
                .hallId(1)
                .build();

        Movie movie = Movie.builder()
                .id(1)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();

        Hall hall = Hall.builder()
                .id(1)
                .name("the best hall")
                .build();


        ScreeningAllDataDto actualResult = mapper.mapFrom(screening, movie, hall);

        ScreeningAllDataDto expectedResult = ScreeningAllDataDto.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(String.valueOf(LocalDateTime.of(2024, 1, 1, 16, 30))
                        .replace("T", " "))
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .hallName("the best hall")
                .imageUrl(null)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
