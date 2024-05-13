package com.vasche.servlet.admin.movie;

import com.vasche.exception.ServiceException;
import com.vasche.service.MovieService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.MOVIES;
import static com.vasche.util.UrlPath.REMOVE_MOVIE;

@WebServlet(REMOVE_MOVIE)
public class RemoveMovieServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RemoveMovieServlet.class);
    private final MovieService movieService = new MovieService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        String movieId = req.getParameter("movieId");

        if (movieId == null || movieId.isEmpty()) {
            LOGGER.error("Invalid movieId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }

        try {
            movieService.delete(Integer.valueOf(movieId));
            resp.sendRedirect(MOVIES);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
