package com.vasche.servlet.admin;

import com.vasche.exception.ServiceException;
import com.vasche.service.HallService;
import com.vasche.service.MovieService;
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

import static com.vasche.util.UrlPath.ADMIN_PANEL;

@WebServlet(ADMIN_PANEL)
public class AdminPanelServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminPanelServlet.class);
    private final ReservationService reservationService = new ReservationService();
    private final ScreeningService screeningService = new ScreeningService();
    private final HallService hallService = new HallService();
    private final MovieService movieService = new MovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("totalIncome", reservationService.countTotalIncome());
            req.setAttribute("moviesNumber", movieService.countNumberOfMovies());
            req.setAttribute("screeningsNumber", screeningService.countNumberOfScreenings());
            req.setAttribute("hallsNumber", hallService.countNumberOfHalls());

            req.getRequestDispatcher(JspHelper.get("admin-panel")).forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
