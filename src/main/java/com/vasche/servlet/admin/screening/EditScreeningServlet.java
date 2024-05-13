package com.vasche.servlet.admin.screening;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.service.ScreeningService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.vasche.util.UrlPath.EDIT_SCREENING;
import static com.vasche.util.UrlPath.SCREENINGS;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(EDIT_SCREENING)
public class EditScreeningServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EditScreeningServlet.class);
    private final ScreeningService screeningService = new ScreeningService();
    private Integer screeningId = null;
    private Integer movieId = null;
    private Integer hallId = null;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {

        if (req.getParameter("screeningId") == null || req.getParameter("screeningId").isEmpty()) {
            LOGGER.error("Invalid screeningId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            screeningId = Integer.parseInt(req.getParameter("screeningId"));
            final Optional<ScreeningDto> screeningDto = screeningService.findById(screeningId);
            if (screeningDto.isPresent()) {
                req.setAttribute("id", screeningId);
                req.setAttribute("price", screeningDto.get().getPrice());
                req.setAttribute("startTime", screeningDto.get().getStartTime());
                req.setAttribute("movieId", Objects.requireNonNullElseGet(movieId, () -> screeningDto.get().getMovieId()));
                req.setAttribute("hallId", Objects.requireNonNullElseGet(hallId, () -> screeningDto.get().getHallId()));
                req.getRequestDispatcher(JspHelper.get("edit-screening")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final CreateScreeningDto createScreeningDto = CreateScreeningDto.builder()
                .id(screeningId.toString())
                .movieId(req.getParameter("movieId"))
                .hallId(req.getParameter("hallId"))
                .price(req.getParameter("price"))
                .startTime(req.getParameter("startTime"))
                .build();
        try {
            screeningService.update(createScreeningDto);
            resp.sendRedirect(SCREENINGS);
        } catch (final ValidationException validationException) {
            req.setAttribute("errors", validationException.getErrors());
            doGet(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("GET")) {
            if (req.getParameter("movieId") != null) {
                movieId = Integer.parseInt(req.getParameter("movieId"));
            }
            if (req.getParameter("hallId") != null) {
                hallId = Integer.parseInt(req.getParameter("hallId"));
            }
            doGet(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
