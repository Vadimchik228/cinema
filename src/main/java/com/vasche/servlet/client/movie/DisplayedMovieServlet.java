package com.vasche.servlet.client.movie;

import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.ScreeningWithHallDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.MovieService;
import com.vasche.service.ScreeningService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.vasche.util.UrlPath.DISPLAYED_MOVIE;

@WebServlet(DISPLAYED_MOVIE + "/*")
public class DisplayedMovieServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DisplayedMovieServlet.class);
    private final MovieService movieService = new MovieService();
    private final ScreeningService screeningService = new ScreeningService();

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
            final List<ScreeningWithHallDto> screenings = screeningService.findAllAvailable(Integer.parseInt(movieId));
            if (movieDto.isPresent()) {
                req.setAttribute("movie", movieDto.get());
                req.setAttribute("screenings", screenings);
                req.getRequestDispatcher(JspHelper.get("displayed-movie")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
