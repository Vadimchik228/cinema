package com.vasche.servlet.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.vasche.util.constants.UrlPath.LOGIN;
import static com.vasche.util.constants.UrlPath.LOGOUT;

@WebServlet(LOGOUT)
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect(LOGIN);
    }
}