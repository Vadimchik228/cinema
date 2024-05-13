package com.vasche.servlet.admin.movie;

import com.vasche.dto.movie.MovieDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.MovieService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.vasche.util.UrlPath.MOVIE;

@WebServlet(MOVIE + "/*")
public class MovieServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MovieServlet.class);
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
                req.getRequestDispatcher(JspHelper.get("movie")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
