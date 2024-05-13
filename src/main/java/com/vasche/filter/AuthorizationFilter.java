package com.vasche.filter;

import com.vasche.dto.user.UserDto;
import com.vasche.entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static com.vasche.util.UrlPath.*;


@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(LOGIN, REGISTRATION, LOGOUT);
    private static final Set<String> CLIENT_PATHS = Set.of(PROFILE,
            DISPLAYED_MOVIES, DISPLAYED_MOVIE,
            CHOOSE_SEAT,
            RESERVATIONS, RESERVATION, DOWNLOAD_PDF);
    private static final Set<String> ADMIN_PATHS = Set.of(PROFILE, ADMIN_PANEL,
            MOVIE, MOVIES, ADD_MOVIE, EDIT_MOVIE, IMAGES, REMOVE_MOVIE,
            SCREENING, SCREENINGS, ADD_SCREENING, EDIT_SCREENING, CHOOSE_MOVIE, CHOOSE_HALL, REMOVE_SCREENING,
            BOOKMAKER, BOOKMAKERS, REMOVE_BOOKMAKER,
            HALLS);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        var uri = ((HttpServletRequest) servletRequest).getRequestURI();

        if ((isAdminPath(uri) && isAdminLogged(servletRequest))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if ((isClientPath(uri) && isClientLogged(servletRequest))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (isPublicPath(uri) || isUserLoggedIn(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            var prevPage = ((HttpServletRequest) servletRequest).getHeader("referer");
            ((HttpServletResponse) servletResponse).sendRedirect(prevPage != null ? prevPage : LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        var user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isAdminLogged(ServletRequest servletRequest) {
        var user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user.getRole() == Role.ADMIN;
    }

    private boolean isClientLogged(ServletRequest servletRequest) {
        var user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user.getRole() == Role.CLIENT;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::startsWith) || isAdminPath(uri) || isClientPath(uri);
    }

    private boolean isAdminPath(String uri) {
        return ADMIN_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isClientPath(String uri) {
        return CLIENT_PATHS.stream().anyMatch(uri::startsWith);
    }
}
