package com.vasche.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vasche.entity.*;
import com.vasche.exception.PdfException;
import com.vasche.exception.RepositoryException;
import com.vasche.repository.*;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.vasche.util.constants.PdfConstants.*;

public class TicketPdfService {
    private static final Logger LOGGER = Logger.getLogger(TicketPdfService.class);
    private final Font headFont;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final LineRepository lineRepository;
    private final HallRepository hallRepository;

    public TicketPdfService() throws PdfException {
        BaseFont unicode;
        try {
            unicode = BaseFont.createFont(FONTS_BAHNSCHRIFT_TTF_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            screeningRepository = new ScreeningRepository();
            userRepository = new UserRepository();
            seatRepository = new SeatRepository();
            lineRepository = new LineRepository();
            hallRepository = new HallRepository();
            movieRepository = new MovieRepository();
        } catch (DocumentException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new PdfException("Couldn't set front for PDF", e);
        }
        headFont = new Font(unicode, HEAD_FONT_SIZE);
    }

    public ByteArrayOutputStream formPdfTicket(final Integer reservationId, Locale locale) throws PdfException, RepositoryException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(COLUMNS_NUMBER);
            table.setWidthPercentage(TABLE_WIDTH_PERCENTAGE);
            table.setTotalWidth(PAGE_WIDTH);
            Optional<Screening> screening = screeningRepository.findByReservationId(reservationId);
            Optional<User> user = userRepository.findByReservationId(reservationId);
            Optional<Seat> seat = seatRepository.findByReservationId(reservationId);
            if (screening.isPresent() && user.isPresent() && seat.isPresent()) {
                Optional<Line> line = lineRepository.findBySeatId(seat.get().getId());
                Optional<Movie> movie = movieRepository.findById(screening.get().getMovieId());
                if (line.isPresent() && movie.isPresent()) {
                    Optional<Hall> hall = hallRepository.findByLineId(line.get().getId());
                    if (hall.isPresent()) {
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
                        addRowToTable(bundle.getString("ticket.pdf.ticketPrice") + ": ", table, ticketPriceInFormat + "$");
                        PdfWriter.getInstance(document, outputStream);
                        document.open();
                        document.add(table);
                        document.close();
                    }
                }
            }
        } catch (DocumentException e) {
            LOGGER.error(e.getMessage());
            throw new PdfException("Couldn't create pdf ticket", e);
        }
        return outputStream;
    }

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
        lCell.setHorizontalAlignment(ALIGN_LEFT);
    }

    private void setRightCell(PdfPCell cell) {
        cell.setVerticalAlignment(ALIGN_MIDDLE);
        cell.setHorizontalAlignment(ALIGN_MIDDLE);
        cell.setPaddingRight(PADDING_VALUE);
    }

    private String getTicketPriceInFormat(Screening screening) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
        df.setMinimumFractionDigits(MINIMUM_FRACTION_DIGITS);
        df.setGroupingUsed(false);
        return df.format(screening.getPrice());
    }
}
