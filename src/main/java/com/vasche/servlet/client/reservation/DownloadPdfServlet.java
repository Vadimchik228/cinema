package com.vasche.servlet.client.reservation;

import com.vasche.exception.PdfException;
import com.vasche.exception.RepositoryException;
import com.vasche.service.TicketPdfService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import static com.vasche.util.UrlPath.DOWNLOAD_PDF;
import static com.vasche.util.constants.PdfConstants.DEF_TICKET_FILENAME;

@WebServlet(DOWNLOAD_PDF)
public class DownloadPdfServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DownloadPdfServlet.class);
    private final TicketPdfService ticketPdfService = new TicketPdfService();

    public DownloadPdfServlet() throws PdfException {
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        String reservationId = req.getParameter("reservationId");

        if (reservationId == null || reservationId.isEmpty()) {
            LOGGER.error("Invalid reservationId parameter");
            resp.sendRedirect(req.getHeader("referer"));
            return;
        }

        try {
            resp.setContentType("application/pdf;charset=UTF-8");
            resp.addHeader("Content-Disposition", "attachment; filename=" + DEF_TICKET_FILENAME + ".pdf");

            ByteArrayOutputStream byteArrayOutputStream = ticketPdfService.formPdfTicket(Integer.parseInt(reservationId), Locale.ENGLISH);
            ServletOutputStream servletOutputStream = resp.getOutputStream();
            byteArrayOutputStream.writeTo(servletOutputStream);
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (PdfException | RepositoryException | NumberFormatException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
