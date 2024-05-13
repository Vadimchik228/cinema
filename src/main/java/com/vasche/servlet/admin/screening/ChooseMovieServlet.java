package com.vasche.servlet.admin.screening;

import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.entity.Genre;
import com.vasche.exception.ServiceException;
import com.vasche.service.MovieService;
import com.vasche.util.JspHelper;
import com.vasche.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.CHOOSE_MOVIE;

@WebServlet(CHOOSE_MOVIE)
public class ChooseMovieServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ChooseMovieServlet.class);
    private final MovieService movieService = new MovieService();
    private MovieFilterDto filter = MovieFilterDto.builder()
            .title(null)
            .genre(null)
            .minimumAge(null)
            .build();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        var prevPage = req.getHeader("referer");
        req.setAttribute("prevPage", UrlPath.transformUrl(prevPage));
        forwardToMovies(filter, req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        var prevPage = req.getHeader("referer");
        req.setAttribute("prevPage", UrlPath.transformUrl(prevPage));
        filter = MovieFilterDto.builder()
                .title(req.getParameter("title"))
                .minimumAge(req.getParameter("minimumAge"))
                .genre(req.getParameter("genre"))
                .build();
        forwardToMovies(filter, req, resp);
    }

    private void forwardToMovies(MovieFilterDto filter, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("movies", movieService.findAllByFilter(filter));
            req.setAttribute("genres", Genre.values());
            req.setAttribute("selectedGenre", filter.getGenre());
            req.setAttribute("selectedTitle", filter.getTitle());
            req.setAttribute("selectedMinimumAge", filter.getMinimumAge());
            req.getRequestDispatcher(JspHelper.get("choose-movie"))
                    .forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}