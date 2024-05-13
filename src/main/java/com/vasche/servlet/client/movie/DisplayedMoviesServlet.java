package com.vasche.servlet.client.movie;

import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.entity.Genre;
import com.vasche.exception.ServiceException;
import com.vasche.service.ScreeningService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.DISPLAYED_MOVIES;

@WebServlet(DISPLAYED_MOVIES)
public class DisplayedMoviesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DisplayedMoviesServlet.class);
    private final ScreeningService screeningService = new ScreeningService();
    private ScreeningFilterDto filter = ScreeningFilterDto.builder()
            .title(null)
            .genre(null)
            .date(null)
            .build();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        forwardToDisplayedMovies(filter, req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        filter = ScreeningFilterDto.builder()
                .title(req.getParameter("title"))
                .genre(req.getParameter("genre"))
                .date(req.getParameter("date"))
                .build();
        forwardToDisplayedMovies(filter, req, resp);
    }

    private void forwardToDisplayedMovies(ScreeningFilterDto filter, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("movies", screeningService.findAllDisplayedMoviesByFilter(filter));
            req.setAttribute("genres", Genre.values());
            req.setAttribute("selectedGenre", filter.getGenre());
            req.setAttribute("selectedTitle", filter.getTitle());
            req.setAttribute("selectedDate", filter.getDate());
            req.getRequestDispatcher(JspHelper.get("displayed-movies")).forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

}
