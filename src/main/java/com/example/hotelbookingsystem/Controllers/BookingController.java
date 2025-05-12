package com.example.hotelbookingsystem.Controllers;


import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<Booking> getBookings() {
        return bookingService.getBookingList();
    }

//    @GetMapping("my")
//    public ResponseEntity<?> getBookingsMy() {
//        List<BookingDTO> bookingListMy   = bookingService.getBookingListMy().stream().map(booking -> new BookingDTO());
//        if (bookingListMy == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have any bookings!");
//        }
//        return ResponseEntity.ok(bookingListMy);
//    }

    @PostMapping("add_booking")
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) {
        Booking saveBooking   = bookingService.saveBooking(booking);
        if (saveBooking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dates are busy");
        }
        return ResponseEntity.ok("Booking was added");
    }



    @PutMapping("update_booking")
    public String updateBooking(@RequestBody Booking booking) {
        bookingService.updateBooking(booking);
        return "Booking was updated";
    }

    @DeleteMapping("delete_booking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);

        return "Booking was deleted by id - " + id;
    }
}
