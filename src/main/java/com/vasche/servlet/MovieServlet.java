package com.vasche.servlet;

import com.vasche.exception.DaoException;
import com.vasche.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/movies")
public class MovieServlet extends HttpServlet {

    private MovieService movieService = MovieService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var printWriter = resp.getWriter()) {
            printWriter.write("<h1>Список фильмов: <h1>");
            printWriter.write("<ul>");
            movieService.findAll().forEach(movieDto -> {
                printWriter.write("""
                        <li>
                            <a href="/screenings?movieId=%d">%s</a>
                        </li>
                        """.formatted(movieDto.getId(), movieDto.getDescription()));
            });
            printWriter.write("</ul>");
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
