package com.vasche.servlet.admin.movie;

import com.vasche.service.ImageService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import static com.vasche.util.UrlPath.IMAGES;

@WebServlet(value = IMAGES + "/*")
public class ImageServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ImageServlet.class);
    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        final String requestURI = req.getRequestURI();
        final String imagePath = requestURI.replace("/images", "");


        imageService.get(imagePath).ifPresentOrElse(image -> {
            resp.setContentType("application/octet-stream");
            try {
                writeImage(image, resp);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }, () -> resp.setStatus(404));

    }

    private void writeImage(final InputStream image, final HttpServletResponse resp) throws IOException {
        try (image; final ServletOutputStream outputStream = resp.getOutputStream()) {
            int currentByte;
            while ((currentByte = image.read()) != -1) {
                outputStream.write(currentByte);
            }
        }
    }
}
