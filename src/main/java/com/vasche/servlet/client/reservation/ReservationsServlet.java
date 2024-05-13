package com.vasche.servlet.client.reservation;

import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.dto.user.UserDto;
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
import java.util.List;

import static com.vasche.util.UrlPath.RESERVATIONS;

@WebServlet(RESERVATIONS)
public class ReservationsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ReservationsServlet.class);
    ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        try {
            List<ReservationAllDataDto> reservations = reservationService.findAllWithAllDataByUserId(userDto.getId());
            req.setAttribute("reservations", reservations);
            req.getRequestDispatcher(JspHelper.get("reservations")).forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

}
