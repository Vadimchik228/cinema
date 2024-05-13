package com.vasche.servlet.admin.screening.bookmaker;

import com.vasche.dto.user.UserWithSeatDto;
import com.vasche.exception.ServiceException;
import com.vasche.service.UserService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.vasche.util.UrlPath.BOOKMAKERS;

@WebServlet(BOOKMAKERS + "/*")
public class BookmakersServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BookmakersServlet.class);
    private final UserService userService = new UserService();
    private String prevPage = null;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("screeningId") == null || req.getParameter("screeningId").isEmpty()) {
            LOGGER.error("Invalid screeningId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }
        try {
            Integer screeningId = Integer.parseInt(req.getParameter("screeningId"));
            List<UserWithSeatDto> bookmakers = userService.findAllBookmakersByScreeningId(screeningId);
            req.setAttribute("bookmakers", bookmakers);
            req.setAttribute("bookmakersNumber", bookmakers.size());

            if (prevPage == null) {
                prevPage = req.getHeader("referer");
            }
            req.setAttribute("prevPage", prevPage);

            req.getRequestDispatcher(JspHelper.get("bookmakers")).forward(req, resp);
        } catch (ServiceException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
