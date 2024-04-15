//package com.vasche.service;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.vasche.dao.ReservationDao;
//import com.vasche.dao.ScreeningDao;
//import com.vasche.entity.Movie;
//import com.vasche.entity.Reservation;
//import com.vasche.entity.Screening;
//import com.vasche.entity.User;
//import com.vasche.exception.DaoException;
//import com.vasche.exception.PdfException;
//import com.vasche.exception.WritePdfToResponseException;
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.util.Locale;
//import java.util.ResourceBundle;
//
//import static com.vasche.util.constants.OtherConstants.DEF_TICKET_FILENAME;
//import static com.vasche.util.constants.OtherConstants.FONTS_BAHNSCHRIFT_TTF_PATH;
//
//public class TicketPdfService {
//    private final Font headFont;
//    private final ScreeningDao screeningDao = ScreeningDao.getInstance();
//
//    private final ReservationDao reservationDao = ReservationDao.getInstance();
//
//    public TicketPdfService() throws PdfException {
//        BaseFont unicode = null;
//        try {
//            unicode = BaseFont.createFont(FONTS_BAHNSCHRIFT_TTF_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        } catch (DocumentException | IOException e) {
//            throw new PdfException("Couldn't set front for PDF", e);
//        }
//        headFont = new Font(unicode, 12);
//    }
//
//    public ByteArrayOutputStream formPDFTicket(Reservation reservation, Locale locale) throws PdfException, DaoException {
//        Document document = new Document();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        try {
//            PdfPTable table = new PdfPTable(2);
//            table.setWidthPercentage(100);
//            table.setTotalWidth(PageSize.A4.getWidth() - 25);
//
//            Screening screening = reservationDao.findScreeningByScreeningId(reservation.getScreeningId());
//            User user = reservationDao.findUserByUserId(reservation.getUserId());
//            Movie movie = screeningDao.findMovieByMovieId(screening.getMovieId());
//
//            final String ticketPriceInFormat = getTicketPriceInFormat(screening);
//
//            final ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
//
//            addRowToTable(bundle.getString("ticket.pdf.user") + ": ", table, String.join(" ", user.getFirstName(), user.getLastName()));
//            addRowToTable(bundle.getString("ticket.pdf.email") + ": ", table, user.getEmail());
//            addRowToTable(bundle.getString("ticket.pdf.film") + ": ", table, movie.getTitle());
//            addRowToTable(bundle.getString("ticket.pdf.date") + ": ", table, String.valueOf(screening.getStartTime().toLocalDate()));
//            addRowToTable(bundle.getString("ticket.pdf.time") + ": ", table, String.valueOf(screening.getStartTime().toLocalTime()));
//            addRowToTable(bundle.getString("ticket.pdf.ticketPrice") + ": ", table, ticketPriceInFormat);
//
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//            document.add(table);
//            document.close();
//        } catch (DocumentException e) {
//            throw new PdfException("Couldn't create pdf ticket", e);
//        }
//
//        return outputStream;
//    }
//
//    public void writePdfToResponse(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response) {
//        try {
//            writeByteArrayOutputStreamToResponse(byteArrayOutputStream, response);
//        } catch (IOException | PdfException e) {
//            throw new WritePdfToResponseException("Couldn't write pdf ticket to HttpServletResponse", e);
//        }
//    }
//
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
//
//    private void addRowToTable(String string, PdfPTable table, String ticket) {
//        PdfPCell lCell;
//        PdfPCell rCell;
//        lCell = new PdfPCell(new Phrase(string, headFont));
//        setLeftCell(lCell);
//        table.addCell(lCell);
//
//        rCell = new PdfPCell(new Phrase(ticket, headFont));
//        setRightCell(rCell);
//        table.addCell(rCell);
//    }
//
//    private void setLeftCell(PdfPCell lCell) {
//        lCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//    }
//
//    private void setRightCell(PdfPCell cell) {
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//        cell.setPaddingRight(5);
//    }
//
//    private String getTicketPriceInFormat(Screening screening) {
//        DecimalFormat df = new DecimalFormat();
//        df.setMaximumFractionDigits(2);
//        df.setMinimumFractionDigits(0);
//        df.setGroupingUsed(false);
//        return df.format(screening.getPrice());
//    }
//}
