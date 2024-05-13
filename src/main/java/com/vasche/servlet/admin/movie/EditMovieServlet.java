package com.vasche.servlet.admin.movie;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Genre;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.service.MovieService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.vasche.util.UrlPath.EDIT_MOVIE;
import static com.vasche.util.UrlPath.MOVIES;


@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(EDIT_MOVIE)
public class EditMovieServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditMovieServlet.class);
    private final MovieService movieService = new MovieService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        String movieId = req.getParameter("movieId");

        if (req.getParameter("movieId") == null || req.getParameter("movieId").isEmpty()) {
            LOGGER.error("Invalid movieId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            final Optional<MovieDto> movieDto = movieService.findById(Integer.parseInt(movieId));
            if (movieDto.isPresent()) {
                req.setAttribute("movieDto", movieDto.get());
                req.setAttribute("genres", Genre.values());
                req.getRequestDispatcher(JspHelper.get("edit-movie")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final CreateMovieDto createMovieDto = CreateMovieDto.builder()
                .title(req.getParameter("title"))
                .description(req.getParameter("description"))
                .durationMin(req.getParameter("durationMin"))
                .minimumAge(req.getParameter("minimumAge"))
                .genre(req.getParameter("genre"))
                .image(req.getPart("image"))
                .build();

        try {
            movieService.update(createMovieDto);
            resp.sendRedirect(MOVIES);
        } catch (final ValidationException validationException) {
            req.setAttribute("errors", validationException.getErrors());
            doGet(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}