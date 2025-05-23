package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> getBookingList();
    List<Booking> getBookingListMy();
    Booking saveBooking(Booking booking);
    Booking findByIdBooking (Long id);
    Booking updateBooking(Booking booking);
    String deleteBooking(Long id);

}
