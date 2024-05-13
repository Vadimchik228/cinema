package com.vasche.servlet.admin.screening;

import com.vasche.dto.screening.CreateScreeningDto;
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

import static com.vasche.util.UrlPath.ADD_SCREENING;
import static com.vasche.util.UrlPath.SCREENINGS;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(ADD_SCREENING)
public class AddScreeningServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddScreeningServlet.class);
    private final ScreeningService screeningService = new ScreeningService();
    private Integer movieId = null;
    private Integer hallId = null;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("movieId", movieId);
        req.setAttribute("hallId", hallId);
        req.getRequestDispatcher(JspHelper.get("add-screening")).forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final CreateScreeningDto screeningDto = CreateScreeningDto.builder()
                .price(req.getParameter("price"))
                .startTime(req.getParameter("startTime"))
                .hallId(req.getParameter("hallId"))
                .movieId(req.getParameter("movieId"))
                .build();
        try {
            screeningService.create(screeningDto);
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
                req.setAttribute("movieId", movieId);
            }
            if (req.getParameter("hallId") != null) {
                hallId = Integer.parseInt(req.getParameter("hallId"));
                req.setAttribute("hallId", hallId);
            }
            doGet(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
