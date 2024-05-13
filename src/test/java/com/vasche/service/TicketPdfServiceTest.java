package com.vasche.service;

import com.vasche.repository.*;
import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.PdfException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TicketPdfServiceTest extends ServiceTestBase {
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    TicketPdfService ticketPdfService;

    @Test
    void formPdfTicket() throws RepositoryException, PdfException {
        Seat seat = getSeat();
        User user = getUser();
        Movie movie = getMovie(1);
        Screening screening = getScreening(1);
        Line line = getLine();
        Hall hall = getHall();

        Integer reservationId = 1;
        Reservation reservation = getReservation(reservationId);

        doReturn(Optional.of(seat)).when(seatRepository).findByReservationId(reservationId);
        doReturn(Optional.of(user)).when(userRepository).findByReservationId(reservationId);
        doReturn(Optional.of(screening)).when(screeningRepository).findByReservationId(reservationId);
        doReturn(Optional.of(line)).when(lineRepository).findBySeatId(seat.getId());
        doReturn(Optional.of(hall)).when(hallRepository).findByLineId(line.getId());
        doReturn(Optional.of(movie)).when(movieRepository).findById(screening.getMovieId());

        ByteArrayOutputStream outputStream = ticketPdfService.formPdfTicket(reservation.getId(), Locale.ENGLISH);
        assertThat(outputStream).isNotNull();
    }
}
