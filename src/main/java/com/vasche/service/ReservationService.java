//package com.vasche.service;
//
//import com.vasche.dao.ReservationDao;
//import com.vasche.dto.reservation.CreateReservationDto;
//import com.vasche.dto.reservation.ReservationDto;
//import com.vasche.entity.Reservation;
//import com.vasche.exception.DaoException;
//import com.vasche.mapper.reservation.CreateReservationMapper;
//import com.vasche.mapper.reservation.ReservationMapper;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static lombok.AccessLevel.PRIVATE;
//
//@NoArgsConstructor(access = PRIVATE)
//public class ReservationService {
//    private static final ReservationService INSTANCE = new ReservationService();
//
//    private final ReservationDao reservationDao = ReservationDao.getInstance();
//
//
//    private final ReservationMapper reservationMapper = ReservationMapper.getInstance();
//
//    private final CreateReservationMapper createReservationMapper = CreateReservationMapper.getInstance();
//
//
//    public static ReservationService getInstance() {
//        return INSTANCE;
//    }
//
//    public BigDecimal countIncomeFromScreening(final Integer screeningId) throws DaoException {
//        BigDecimal result = BigDecimal.valueOf(0, 2);
//
//        List<ReservationDto> reservationDtos = reservationDao.findAllByScreeningId(screeningId)
//                .stream().map(reservationMapper::mapFrom)
//                .toList();
//
//        while (reservationDtos.listIterator().hasNext()) {
//            var reservationDto = reservationDtos.listIterator().next();
//            var screening = reservationDao.findScreeningByScreeningId(reservationDto.getScreeningId());
//            result.add(screening.getPrice());
//        }
//        return result;
//    }
//
//    public BigDecimal countTotalIncome() throws DaoException {
//        BigDecimal result = BigDecimal.valueOf(0, 2);
//
//        List<ReservationDto> reservationDtos = reservationDao.findAll()
//                .stream().map(reservationMapper::mapFrom)
//                .toList();
//
//        while (reservationDtos.listIterator().hasNext()) {
//            var reservationDto = reservationDtos.listIterator().next();
//            var screening = reservationDao.findScreeningByScreeningId(reservationDto.getScreeningId());
//            result.add(screening.getPrice());
//        }
//        return result;
//    }
//
//    public List<ReservationDto> findAllByUserId(final Integer userId) throws DaoException {
//        return reservationDao.findAllByUserId(userId).stream()
//                .map(reservationMapper::mapFrom)
//                .toList();
//    }
//
//    public List<ReservationDto> findAllByScreeningId(final Integer screeningId) throws DaoException {
//        return reservationDao.findAllByScreeningId(screeningId).stream()
//                .map(reservationMapper::mapFrom)
//                .toList();
//    }
//
//    public Integer create(final CreateReservationDto createReservationDto) throws DaoException {
//        final Reservation reservation = createReservationMapper.mapFrom(createReservationDto);
//        return reservationDao.save(reservation).getId();
//    }
//
//    public boolean remove(final Integer id) throws DaoException {
//        return reservationDao.delete(id);
//    }
//}
