package com.vasche.servlet;

import com.vasche.entity.Role;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.service.UserService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.vasche.util.UrlPath.*;


@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class);
    private final UserService userService = new UserService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            req.getRequestDispatcher(JspHelper.get("login")).forward(req, resp);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        try {
            var userDto = userService.login(email, password);
            if (userDto.isPresent()) {
                req.getSession().setAttribute("user", userDto.get());
                if (userDto.get().getRole() == Role.ADMIN) {
                    resp.sendRedirect(ADMIN_PANEL);
                } else if (userDto.get().getRole() == Role.CLIENT) {
                    resp.sendRedirect(DISPLAYED_MOVIES);
                }
            }
        } catch (ValidationException e) {
            resp.sendRedirect(LOGIN + "?error&email=" + req.getParameter("email"));
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

}