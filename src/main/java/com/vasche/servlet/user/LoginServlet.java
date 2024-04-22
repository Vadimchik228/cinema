package com.vasche.servlet.user;

import com.vasche.dto.user.UserDto;
import com.vasche.service.UserService;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.vasche.util.constants.UrlPath.LOGIN;


@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            req.getRequestDispatcher(JspHelper.get("login"))
                    .forward(req, resp);
        }
//        else {
//            //resp.sendRedirect(FILMS);
//        }
    }

    @SneakyThrows
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) {
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        userService.login(email, password).ifPresentOrElse(
                userDto -> onLoginSuccess(userDto, req, resp),
                () -> onLoginFail(req, resp)
        );
    }

    @SneakyThrows
    private void onLoginFail(final HttpServletRequest req, final HttpServletResponse resp) {
        resp.sendRedirect(LOGIN + "?error&email=" + req.getParameter("email"));
    }

    @SneakyThrows
    private void onLoginSuccess(final UserDto userDto, final HttpServletRequest req, final HttpServletResponse resp) {
        req.getSession().setAttribute("user", userDto);
        //resp.sendRedirect(FILMS);
    }
}