package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {

    List<BookingDTO> getBookingList();
    List<BookingDTO> getBookingListMy();
    Boolean saveBooking(Booking booking);
    Boolean saveBookingEmailConfirmation(Booking booking);
    Booking findByIdBooking (Long id);
    Booking updateBooking(Booking booking);

    Booking updateBookingPartial(Long id,  Map<String, Object> updates);

    List<BookingDTO> getMyBookingDateRange(LocalDate dateFrom, LocalDate dateTo);
    String deleteBooking(Long id);

}
