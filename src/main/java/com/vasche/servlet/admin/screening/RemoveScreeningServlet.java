package com.vasche.servlet.admin.screening;

import com.vasche.exception.ServiceException;
import com.vasche.service.ScreeningService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.MOVIES;
import static com.vasche.util.UrlPath.REMOVE_SCREENING;

@WebServlet(REMOVE_SCREENING)
public class RemoveScreeningServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RemoveScreeningServlet.class);
    private final ScreeningService screeningService = new ScreeningService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        String screeningId = req.getParameter("screeningId");
        if (screeningId == null || screeningId.isEmpty()) {
            LOGGER.error("Invalid screeningId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            screeningService.delete(Integer.valueOf(screeningId));
            resp.sendRedirect(MOVIES);
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
