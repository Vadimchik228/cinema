package com.vasche.servlet.admin.screening;

import com.vasche.exception.ServiceException;
import com.vasche.service.HallService;
import com.vasche.servlet.admin.movie.RemoveMovieServlet;
import com.vasche.util.JspHelper;
import com.vasche.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.CHOOSE_HALL;

@WebServlet(CHOOSE_HALL)
public class ChooseHallServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ChooseHallServlet.class);
    private final HallService hallService = new HallService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        var prevPage = req.getHeader("referer");
        req.setAttribute("prevPage", UrlPath.transformUrl(prevPage));
        try {
            req.setAttribute("halls", hallService.findAll());
            req.getRequestDispatcher(JspHelper.get("choose-hall")).forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

}
