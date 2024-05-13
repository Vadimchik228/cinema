package com.vasche.servlet.admin.screening;

import com.vasche.dto.screening.ScreeningAllDataDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.ReservationService;
import com.vasche.service.ScreeningService;

import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.vasche.util.UrlPath.SCREENING;

@WebServlet(SCREENING + "/*")
public class ScreeningServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ScreeningServlet.class);
    private final ScreeningService screeningService = new ScreeningService();
    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        String screeningId = req.getParameter("screeningId");

        if (screeningId == null || screeningId.isEmpty()) {
            LOGGER.error("Invalid screeningId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            final Optional<ScreeningAllDataDto> screeningDto = screeningService.findWithAllDataById(Integer.parseInt(screeningId));
            if (screeningDto.isPresent()) {
                req.setAttribute("screeningDto", screeningDto.get());
                req.setAttribute("income", reservationService.countIncomeByScreeningId(screeningDto.get().getId()));
                req.getRequestDispatcher(JspHelper.get("screening")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}