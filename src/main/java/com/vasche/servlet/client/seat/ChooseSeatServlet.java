package com.vasche.servlet.client.seat;

import com.vasche.dto.hall.HallAllDataDto;
import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.user.UserDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.HallService;
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

import static com.vasche.util.UrlPath.CHOOSE_SEAT;
import static com.vasche.util.UrlPath.RESERVATION;

@WebServlet(CHOOSE_SEAT)
public class ChooseSeatServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ChooseSeatServlet.class);
    private final HallService hallService = new HallService();
    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String hallId = req.getParameter("hallId");
        String screeningId = req.getParameter("screeningId");

        if (hallId == null || hallId.isEmpty() || screeningId == null || screeningId.isEmpty()) {
            LOGGER.error("Invalid hallId or screeningId parameters");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }

        try {
            final Optional<HallAllDataDto> hallAllDataDto = hallService.findWithAllDataById(Integer.parseInt(hallId),
                    Integer.parseInt(screeningId));
            if (hallAllDataDto.isPresent()) {
                req.setAttribute("hallDto", hallAllDataDto.get());
                req.setAttribute("screeningId", screeningId);
                req.getRequestDispatcher(JspHelper.get("choose-seat")).forward(req, resp);
            }
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String screeningId = req.getParameter("screeningId");
        UserDto userDto = ((UserDto) req.getSession().getAttribute("user"));
        String seatId = req.getParameter("seatId");

        if (screeningId == null || screeningId.isEmpty() || seatId == null || seatId.isEmpty()) {
            LOGGER.error("Invalid screeningId or seatId parameters");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }

        try {
            final CreateReservationDto createReservationDto = CreateReservationDto.builder()
                    .seatId(seatId)
                    .screeningId(screeningId)
                    .userId(String.valueOf(userDto.getId()))
                    .build();

            final Integer reservationId = reservationService.create(createReservationDto);
            req.setAttribute("reservationId", reservationId);
            resp.sendRedirect(RESERVATION + "?reservationId=" + reservationId);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
