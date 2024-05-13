package com.vasche.servlet.admin.screening.bookmaker;

import com.vasche.exception.ServiceException;
import com.vasche.service.ReservationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.BOOKMAKERS;
import static com.vasche.util.UrlPath.REMOVE_BOOKMAKER;

@WebServlet(REMOVE_BOOKMAKER)
public class RemoveBookmakerServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RemoveBookmakerServlet.class);
    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        String reservationId = req.getParameter("reservationId");
        if (reservationId == null || reservationId.isEmpty()) {
            LOGGER.error("Invalid reservationId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            var reservation = reservationService.findById(Integer.parseInt(reservationId));
            if (reservation.isPresent()) {
                reservationService.delete(Integer.valueOf(reservationId));
                resp.sendRedirect(BOOKMAKERS + "?screeningId=" + reservation.get().getScreeningId());
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
