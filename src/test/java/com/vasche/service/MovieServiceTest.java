package com.vasche.service;

import com.vasche.dao.MovieDao;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.exception.DaoException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.validator.CreateMovieValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.vasche.constant.TestConstant.MOVIE1;

//@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

//    @Mock
//    private CreateMovieValidator createMovieValidator;
//    @Mock
//    private MovieDao movieDao;
//    @Mock
//    private CreateMovieMapper createMovieMapper;
//    @Mock
//    private MovieMapper movieMapper;
//    @InjectMocks
//    private MovieService movieService;

    private MovieService movieService = MovieService.getInstance();

    @Test
    void save() throws IOException, DaoException {
        CreateMovieDto createMovieDto = CreateMovieDto.builder()
                .title("Some Title")
                .genre(Genre.DRAMA.name())
                .durationMin("50")
                .minimumAge("6")
                .description("Some description")
                .image(null)
                .build();
        movieService.create(createMovieDto);
    }

    @Test
    void findAll() throws DaoException {
        List<MovieDto> movieDtos = movieService.findAll();

        System.out.println();
    }

//    @Test
//    void findByFilmId() throws DaoException {
//        int movieId = 1;
//        Movie movie = getMovie(movieId);
//        MovieDto movieDto = getMovieDto(movieId);
//        when(movieDao.findById(movieId))
//                .thenReturn(Optional.ofNullable(movie));
//        when(movieMapper.mapFrom(movie))
//                .thenReturn(movieDto);
//
//        List<FeedbackDto> actualResult = feedbackService.findByFilmId(filmId);
//
//        assertThat(actualResult)
//                .hasSize(1);
//        assertThat(actualResult.get(0))
//                .isEqualTo(feedbackDto);
//    }
//
//    @Test
//    void testFindByFilmId() {
//        int filmId = 1;
//        FeedbackEntity feedbackEntity = getFeedbackEntity(filmId);
//        FeedbackDto feedbackDto = getFeedbackDto(filmId);
//        when(feedbackDao.findByFilmId(filmId))
//                .thenReturn(List.of(feedbackEntity));
//        when(feedbackMapper.map(feedbackEntity))
//                .thenReturn(feedbackDto);
//
//        List<FeedbackDto> actualResult = feedbackService.findByFilmId(filmId);
//
//        assertThat(actualResult)
//                .hasSize(1);
//        assertThat(actualResult.get(0))
//                .isEqualTo(feedbackDto);
//    }
//
//
//    @Test
//    void testCreate() {
//        CreateFeedbackDto createFeedbackDto = getCreateFeedbackDto();
//        FeedbackEntity feedbackEntity = getFeedbackEntity(1);
//        when(createFeedbackMapper.map(createFeedbackDto))
//                .thenReturn(feedbackEntity);
//        when(feedbackDao.save(feedbackEntity))
//                .thenReturn(feedbackEntity);
//
//        int actualResult = feedbackService.create(createFeedbackDto);
//
//        assertThat(actualResult)
//                .isEqualTo(1);
//    }
//
//    @Test
//    void testFindByUserName() {
//        int defaultId = 1;
//        String userName = "user";
//        FeedbackEntity feedbackEntity = getFeedbackEntity(1);
//        FeedbackDto feedbackDto = getFeedbackDto(defaultId);
//        when(userService.findIdByName(userName))
//                .thenReturn(Optional.of(defaultId));
//        when(feedbackDao.findByUserId(defaultId))
//                .thenReturn(List.of(feedbackEntity));
//        when(feedbackMapper.map(feedbackEntity))
//                .thenReturn(feedbackDto);
//
//        List<FeedbackDto> actualResult = feedbackService.findByUserName(userName);
//
//        assertThat(actualResult)
//                .hasSize(1);
//        assertThat(actualResult.get(0))
//                .isEqualTo(feedbackDto);
//    }
//
//    @Test
//    void loginSuccess() {
//        User user = getUser();
//        UserDto userDto = getUserDto();
//        doReturn(Optional.of(user)).when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());
//        doReturn(userDto).when(userMapper).map(user);
//
//        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());
//
//        assertThat(actualResult).isPresent();
//        assertThat(actualResult.get()).isEqualTo(userDto);
//    }
//
//    @Test
//    void loginFailed() {
//        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());
//
//        Optional<UserDto> actualResult = userService.login("dummy", "123");
//
//        assertThat(actualResult).isEmpty();
//        verifyNoInteractions(userMapper);
//    }
//
//    @Test
//    void create() {
//        CreateUserDto createUserDto = getCreateUserDto();
//        User user = getUser();
//        UserDto userDto = getUserDto();
//        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);
//        doReturn(user).when(createUserMapper).map(createUserDto);
//        doReturn(userDto).when(userMapper).map(user);
//
//        UserDto actualResult = userService.create(createUserDto);
//
//        assertThat(actualResult).isEqualTo(userDto);
//        verify(userDao).save(user);
//    }
//
//    @Test
//    void shouldThrowExceptionIfDtoInvalid() {
//        CreateUserDto createUserDto = getCreateUserDto();
//        ValidationResult validationResult = new ValidationResult();
//        validationResult.add(Error.of("invalid.role", "message"));
//        doReturn(validationResult).when(createUserValidator).validate(createUserDto);
//
//        assertThrows(ValidationException.class, () -> userService.create(createUserDto));
//        verifyNoInteractions(userDao, createUserMapper, userMapper);
//    }

    private CreateMovieDto getCreateMovieDto() {
        return CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();
    }

    private MovieDto getMovieDto(Integer movieId) {
        return MovieDto.builder()
                .id(movieId)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();
    }

    private Movie getMovie(Integer movieId) {
        return Movie.builder()
                .id(movieId)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();
    }
}
