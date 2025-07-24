package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.BookingRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.RoomService;
import com.example.hotelbookingsystem.service.UserNService;
import com.example.hotelbookingsystem.service.emailService.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
@Primary
public class BookingServiceImpl implements BookingService {

    private  BookingRepository bookingRepository;
    private final RoomService roomService;
    private final EmailService emailService;
    private final UserNService userNService;



//    @Override
//    @Cacheable(value = "booking")
//    public List<Booking> getBookingList() {
//        return bookingRepository.findAll();
//
//    }
    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "booking")
    public List<BookingDTO> getBookingList() {
        List<BookingDTO> bookingDTOList = bookingRepository.findAllBooking()
                .stream()
                .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getDateFrom(),
                        booking.getDateTo(),
                        booking.getRoom().getId(),
                        booking.getUserN().getId()
                )).toList();
        return bookingDTOList;
    }

    @Override
    @Cacheable(value = "bookingMy")
    public List<BookingDTO> getBookingListMy() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();

            return  bookingRepository.findBookingByUserN(userN)
                                                                .stream()
                                                                .map(booking -> new BookingDTO(
                                                                        booking.getId(),
                                                                        booking.getDateFrom(),
                                                                        booking.getDateTo(),
                                                                        booking.getRoom().getId(),
                                                                        booking.getUserN().getId()
                                                                )).toList();
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "booking")
    public List<BookingDTO> getMyBookingDateRange(LocalDate dateFrom, LocalDate dateTo) {
        List<BookingDTO> bookingListMy   = getBookingListMy()
                                                            .stream()
                                                            .filter(b -> b.getDateFrom().isAfter(dateFrom.minusDays(1))
                                                                    &&   b.getDateTo().isBefore(dateTo.plusDays(1))
                                                            ).toList();
        return bookingListMy;
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Boolean saveBookingEmailConfirmation(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (
                bookingRepository.findRoomsByID(
                                    booking.getDateFrom(),
                                    booking.getDateTo(),
                                    booking.getRoom().getId()
            ).isEmpty()) {
            bookingRepository.save(booking);

            Room room = roomService.findRoomWithHotel(booking.getRoom().getId()).orElse(null);
            String nameHotel = room.getHotel().getName();
            String msg = "Hello ,\n thank you for booking a room, starts at "
                    + booking.getDateFrom()
                    + ", ends " + booking.getDateTo();

            if (!(auth instanceof AnonymousAuthenticationToken)) {
                UserN userN = (UserN) auth.getPrincipal();
                emailService.sendSimpleEmail(
                        userN.getEmail(),
                        "Confirming booking at hotel " + nameHotel,
                        msg
                );
            }
            return  true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Boolean saveBooking(Booking booking) {
        if (
                bookingRepository.findRoomsByID(
                        booking.getDateFrom(),
                        booking.getDateTo(),
                        booking.getRoom().getId()
                ).isEmpty()) {
            bookingRepository.save(booking);
            return  true;
        }
        return false;
    }



    @Override
    @Cacheable(value = "booking",key = "#id")
    public Booking findByIdBooking(Long id) {
        return bookingRepository.findAllById(id);
    }

    @Override
    @CachePut(value = "booking",key = "#booking.id")
    public Booking updateBooking(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();
            if (userN.getRole().equals("ROLE_USER")) {
                if (!bookingRepository.findBookingByUserNAndId(userN,booking.getId()).isEmpty()) {
                    bookingRepository.save(booking);
                }
            }
        }

        return bookingRepository.save(booking);
    }

    @Override
    @CachePut(value = "booking", key = "#result.id")
    public Booking updateBookingPartial(Long id,  Map<String, Object> updates) {
        Booking booking = bookingRepository.findAllById(id);
        if (booking == null) {
             return null;
        } else {

            if (updates.containsKey("dateFrom")) {
                booking.setDateFrom(LocalDate.parse(String.valueOf(updates.get("dateFrom"))));
            }
            if (updates.containsKey("dateTo")) {
                booking.setDateTo(LocalDate.parse(String.valueOf(updates.get("dateTo"))));
            }
            if (updates.containsKey("userNId")) {
                booking.setUserN(userNService.findByIdUser(Long.valueOf(updates.get("userNId").toString())));
            }
            if (updates.containsKey("roomId")) {
                booking.setRoom(roomService.findByIdRoom((Long) updates.get("roomId")).orElse(null));
            }
            saveBooking(booking);
            return booking;
        }
    }

    @Override
    @CacheEvict(value = "booking",key = "#id")
    public String deleteBooking(Long id) {
        Booking bookingToDelete = bookingRepository.findAllById(id);
        if (!LocalDate.now().isBefore( bookingToDelete.getDateFrom())) {
            return "You can't anymore cancel your booking";
        } else {
            bookingRepository.deleteById(id);
            return "Booking  was canceled  with id = " + id;
        }

    }
}
