package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.BookingRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


@Service
@AllArgsConstructor
@Primary
public class BookingServiceImpl implements BookingService {

    private  BookingRepository bookingRepository;
    private RoomRepository roomRepository;

    @Override
    public List<Booking> getBookingList() {
        return bookingRepository.findAll();
    }

    @Override
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
    public Booking saveBooking(Booking booking) {
        Room room = roomRepository.findAllById(booking
                                    .getRoom().getId())
                                    .orElseThrow(() -> new RuntimeException("Не найдена"));
        if (
                bookingRepository.findRoomsByID(
                                    booking.getDateFrom(),
                                    booking.getDateTo(),
                                    booking.getRoom().getId()
                ).isEmpty()) {
            System.out.println("start sleep-------------------------------");
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("stop sleep+++++++++++++++++++++++++++++++++");

            return bookingRepository.save(booking);
        }
        return null;
    }



    @Override
    public Booking findByIdBooking(Long id) {
        return bookingRepository.findAllById(id);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
