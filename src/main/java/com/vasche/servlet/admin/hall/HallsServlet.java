package com.vasche.servlet.admin.hall;

import com.vasche.exception.ServiceException;
import com.vasche.service.HallService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.HALLS;

@WebServlet(HALLS)
public class HallsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HallsServlet.class);
    private final HallService hallService = new HallService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("halls", hallService.findAllWithAllData(null));
            req.getRequestDispatcher(JspHelper.get("halls")).forward(req, resp);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
