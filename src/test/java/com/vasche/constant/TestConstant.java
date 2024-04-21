package com.vasche.constant;

import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TestConstant {
    public final static Movie MOVIE1 = Movie.builder()
            .id(1)
            .title("First Film")
            .description("Some description")
            .minimumAge(0)
            .durationMin(50)
            .genre(Genre.DRAMA)
            .build();

    public final static Movie MOVIE2 = Movie.builder()
            .id(2)
            .title("Some Film")
            .description("Some description")
            .minimumAge(6)
            .durationMin(50)
            .genre(Genre.COMEDY)
            .build();

    public final static Movie MOVIE3 = Movie.builder()
            .id(3)
            .title("Some New Film")
            .description("Some description")
            .minimumAge(6)
            .durationMin(50)
            .genre(Genre.COMEDY)
            .build();

    public final static Movie MOVIE4 = Movie.builder()
            .id(4)
            .title("Fourth Film")
            .description("Some description")
            .minimumAge(12)
            .durationMin(50)
            .genre(Genre.DRAMA)
            .build();

    public final static Movie MOVIE5 = Movie.builder()
            .id(5)
            .title("Fifth Film")
            .description("Some description")
            .minimumAge(16)
            .durationMin(50)
            .genre(Genre.DRAMA)
            .build();

    public final static Map<String, Integer> MOVIE_MAP_OF_ATTRIBUTE_AND_NUMBER = Map.of(
            "title", 1,
            "genre", 2,
            "minimum_age", 3
    );
    public final static Map<String, Object> MOVIE_MAP_OF_ATTRIBUTE_AND_VALUE = Map.of(
            "title", "Some",
            "genre", "COMEDY",
            "minimum_age", 6
    );

    public final static String MOVIE_CONDITION = """
            WHERE title LIKE concat(ltrim(?), '%') AND
                  genre LIKE ? AND
                  minimum_age = ?
            """;

    public final static List<Movie> RESULT_LIST_OF_MOVIES = List.of(
            MOVIE2, MOVIE3
    );

    public final static Screening SCREENING1 = Screening.builder()
            .id(1)
            .price(BigDecimal.valueOf(1000, 2))
            .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
            .movieId(1)
            .hallId(1)
            .build();

    public final static Screening SCREENING2 = Screening.builder()
            .id(2)
            .price(BigDecimal.valueOf(2000, 2))
            .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
            .movieId(1)
            .hallId(1)
            .build();

    public final static Screening SCREENING3 = Screening.builder()
            .id(3)
            .price(BigDecimal.valueOf(1500, 2))
            .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
            .movieId(4)
            .hallId(1)
            .build();

    public final static Screening SCREENING4 = Screening.builder()
            .id(4)
            .price(BigDecimal.valueOf(400, 2))
            .startTime(LocalDateTime.of(2024, 1, 4, 12, 30))
            .movieId(1)
            .hallId(1)
            .build();

    public final static Screening SCREENING5 = Screening.builder()
            .id(5)
            .price(BigDecimal.valueOf(600, 2))
            .startTime(LocalDateTime.of(2024, 1, 5, 12, 30))
            .movieId(3)
            .hallId(1)
            .build();

    public final static Map<String, Integer> SCREENING_MAP_OF_ATTRIBUTE_AND_NUMBER = Map.of(
            "genre", 1,
            "date", 2
    );
    public final static Map<String, Object> SCREENING_MAP_OF_ATTRIBUTE_AND_VALUE = Map.of(
            "genre", "COMEDY",
            "date", LocalDate.of(2024, 1, 1)
    );

    public final static String SCREENING_CONDITION = """
            WHERE m.genre LIKE ? AND 
                  DATE(start_time) = ?
            """;

    public final static String CLEAN_MOVIES_SQL = "DELETE FROM movies;";
    public final static String CLEAN_SEATS_SQL = "DELETE FROM seats;";
    public final static String CLEAN_HALLS_SQL = "DELETE FROM halls;";
    public final static String CLEAN_LINES_SQL = "DELETE FROM lines;";
    public final static String CLEAN_SCREENINGS_SQL = "DELETE FROM screenings;";
    public final static String CLEAN_RESERVATIONS_SQL = "DELETE FROM reservations;";
    public final static String CLEAN_USERS_SQL = "DELETE FROM users;";

    public static final String HALL_NAME1 = "Hall 1";
    public static final String HALL_NAME2 = "Hall 2";
    public static final String HALL_NAME3 = "Hall 3";
    public static final String MOVIE_TITLE1 = "1 + 1";
    public static final String MOVIE_TITLE2 = "Spider man";
    public static final String MOVIE_TITLE3 = "It";
    public static final String EMAIL1 = "schebetovskiy@gmail.com";
    public static final String EMAIL2 = "vadimschebetovskiy@gmail.com";
    public static final String EMAIL3 = "schebetovskiyvadim@gmail.com";
}
