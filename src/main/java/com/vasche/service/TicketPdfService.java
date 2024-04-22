package com.vasche.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vasche.repository.*;
import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.PdfException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.vasche.util.constants.OtherConstants.FONTS_BAHNSCHRIFT_TTF_PATH;

public class TicketPdfService {
    private final Font headFont;
    private final ScreeningRepository screeningDao;
    private final SeatRepository seatDao;
    private final UserRepository userDao;
    private final MovieRepository movieDao;
    private final LineRepository lineDao;
    private final HallRepository hallDao;

    public TicketPdfService() throws PdfException {
        BaseFont unicode = null;
        try {
            unicode = BaseFont.createFont(FONTS_BAHNSCHRIFT_TTF_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            screeningDao = new ScreeningRepository();
            userDao = new UserRepository();
            seatDao = new SeatRepository();
            lineDao = new LineRepository();
            hallDao = new HallRepository();
            movieDao = new MovieRepository();
        } catch (DocumentException | IOException e) {
            throw new PdfException("Couldn't set front for PDF", e);
        }
        headFont = new Font(unicode, 12);
    }

    public ByteArrayOutputStream formPdfTicket(Reservation reservation, Locale locale) throws PdfException, RepositoryException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setTotalWidth(PageSize.A4.getWidth() - 25);

            Optional<Screening> screening = screeningDao.findByReservationId(reservation.getId());
            Optional<User> user = userDao.findByReservationId(reservation.getId());
            Optional<Seat> seat = seatDao.findByReservationId(reservation.getId());
            Optional<Line> line = lineDao.findBySeatId(seat.get().getId());
            Optional<Hall> hall = hallDao.findByLineId(line.get().getId());
            Optional<Movie> movie = movieDao.findById(screening.get().getMovieId());

            final String ticketPriceInFormat = getTicketPriceInFormat(screening.get());

            final ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);

            addRowToTable(bundle.getString("ticket.pdf.user") + ": ", table, String.join(" ",
                    user.get().getFirstName(), user.get().getLastName()));
            addRowToTable(bundle.getString("ticket.pdf.email") + ": ", table, user.get().getEmail());
            addRowToTable(bundle.getString("ticket.pdf.movie") + ": ", table, movie.get().getTitle());
            addRowToTable(bundle.getString("ticket.pdf.minimumAge") + ": ", table, String.valueOf(movie.get().getMinimumAge()));
            addRowToTable(bundle.getString("ticket.pdf.durationMin") + ": ", table, String.valueOf(movie.get().getDurationMin()));
            addRowToTable(bundle.getString("ticket.pdf.date") + ": ", table, String.valueOf(screening.get().getStartTime().toLocalDate()));
            addRowToTable(bundle.getString("ticket.pdf.time") + ": ", table, String.valueOf(screening.get().getStartTime().toLocalTime()));
            addRowToTable(bundle.getString("ticket.pdf.hall") + ": ", table, hall.get().getName());
            addRowToTable(bundle.getString("ticket.pdf.line") + ": ", table, String.valueOf(line.get().getNumber()));
            addRowToTable(bundle.getString("ticket.pdf.seat") + ": ", table, String.valueOf(seat.get().getNumber()));
            addRowToTable(bundle.getString("ticket.pdf.ticketPrice") + ": ", table, ticketPriceInFormat);

            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new PdfException("Couldn't create pdf ticket", e);
        }

        return outputStream;
    }

//    public void writePdfToResponse(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response) {
//        try {
//            writeByteArrayOutputStreamToResponse(byteArrayOutputStream, response);
//        } catch (IOException | PdfException e) {
//            throw new WritePdfToResponseException("Couldn't write pdf ticket to HttpServletResponse", e);
//        }
//    }

//    private void writeByteArrayOutputStreamToResponse(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response)
//            throws IOException, PdfException {
//        try {
//            // ВОТ ЭТО НАДО БУДЕТ ДОБАВИТЬ В СЕРВЛЕТ!!!!
//            response.setContentType("application/pdf");
//            response.addHeader("Content-Disposition", "inline; filename=" + DEF_TICKET_FILENAME + ".pdf");
//            // ВОТ ЭТО НАДО БУДЕТ ДОБАВИТЬ В СЕРВЛЕТ!!!!
//            ServletOutputStream servletOutputStream = response.getOutputStream();
//            byteArrayOutputStream.writeTo(servletOutputStream);
//        } catch (Exception e) {
//            throw new PdfException("Handled error when trying to write PDF to output", e);
//        } finally {
//            if (byteArrayOutputStream != null) {
//                byteArrayOutputStream.flush();
//                byteArrayOutputStream.close();
//            }
//        }
//    }

    private void addRowToTable(String string, PdfPTable table, String ticket) {
        PdfPCell lCell;
        PdfPCell rCell;
        lCell = new PdfPCell(new Phrase(string, headFont));
        setLeftCell(lCell);
        table.addCell(lCell);

        rCell = new PdfPCell(new Phrase(ticket, headFont));
        setRightCell(rCell);
        table.addCell(rCell);
    }

    private void setLeftCell(PdfPCell lCell) {
        lCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    }

    private void setRightCell(PdfPCell cell) {
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingRight(5);
    }

    private String getTicketPriceInFormat(Screening screening) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(screening.getPrice());
    }
}
