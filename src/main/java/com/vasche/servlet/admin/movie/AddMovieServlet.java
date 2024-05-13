package com.vasche.servlet.admin.movie;

import com.vasche.dto.movie.CreateMovieDto;
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

import static com.vasche.util.UrlPath.ADD_MOVIE;
import static com.vasche.util.UrlPath.MOVIES;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(ADD_MOVIE)
public class AddMovieServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddMovieServlet.class);
    private final MovieService movieService = new MovieService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genres", Genre.values());
        req.getRequestDispatcher(JspHelper.get("add-movie")).forward(req, resp);
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
            movieService.create(createMovieDto);
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
