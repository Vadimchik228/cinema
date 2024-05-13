package com.vasche.servlet;

import com.vasche.dto.user.UserDto;
import com.vasche.util.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.vasche.util.UrlPath.PROFILE;

@WebServlet(PROFILE)
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final UserDto userDto = (UserDto) req.getSession().getAttribute("user");
        req.setAttribute("user", userDto);
        req.setAttribute("prevPage", req.getHeader("referer"));
        req.getRequestDispatcher(JspHelper.get("profile")).forward(req, resp);
    }
}
