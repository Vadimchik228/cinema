package com.vasche.servlet.admin.screening.bookmaker;

import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.ReservationService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.vasche.util.UrlPath.BOOKMAKER;
import static com.vasche.util.UrlPath.LOGIN;

@WebServlet(BOOKMAKER)
public class BookmakerServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BookmakerServlet.class);
    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        String prevPage = req.getHeader("referer");
        String reservationId = req.getParameter("reservationId");
        if (reservationId == null || reservationId.isEmpty()) {
            LOGGER.error("Invalid reservationId parameter");
            resp.sendRedirect(prevPage);
            return;
        }
        try {
            final Optional<ReservationAllDataDto> reservationDto = reservationService.findWithALlDataById(Integer.parseInt(reservationId));
            if (reservationDto.isPresent()) {
                req.setAttribute("reservationDto", reservationDto.get());
                req.setAttribute("prevPage", prevPage != null ? prevPage : LOGIN);
                req.getRequestDispatcher(JspHelper.get("bookmaker")).forward(req, resp);
            }
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }

    }

}
