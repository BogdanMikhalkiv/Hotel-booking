package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.BookingRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.RoomService;
import com.example.hotelbookingsystem.service.emailService.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
@Primary
public class BookingServiceImpl implements BookingService {

    private  BookingRepository bookingRepository;
    private final RoomService roomService;
    private final EmailService emailService;


    private RoomRepository roomRepository;

    @Override
    @Cacheable(value = "booking")
    public List<Booking> getBookingList() {
        return bookingRepository.findAll();
    }

    @Override
    @Cacheable(value = "booking")
    public List<Booking> getBookingListMy() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            // userDetails = auth.getPrincipal()
            UserN userN = (UserN) auth.getPrincipal();
            return  bookingRepository.findBookingByUserN(userN);
        }
        return null;
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
            Room room = roomService.findRoomWithHotel(booking.getRoom().getId()).orElse(null);
            String nameHotel = room.getHotel().getName();
            String msg = "Hello ,\n thank you for booking a room, starts at "
                    + booking.getDateFrom()
                    + ", ends " + booking.getDateTo();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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



    @Override
    @Cacheable(value = "booking",key = "#id")
    public Booking findByIdBooking(Long id) {
        return bookingRepository.findAllById(id);
    }

    @Override
    @Cacheable(value = "booking",key = "#booking.id")
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Cacheable(value = "booking",key = "#id")
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
