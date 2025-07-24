package com.example.hotelbookingsystem.Controllers;


import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.HotelService;
import com.example.hotelbookingsystem.service.RoomService;
import com.example.hotelbookingsystem.service.UserNService;
import com.example.hotelbookingsystem.service.emailService.EmailService;
import com.example.hotelbookingsystem.service.impl.BookingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;
    private  final UserNService userNService;
    private final RoomService roomService;
    private final EmailService emailService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings() {
        System.out.println("до выполнения ");
        List<BookingDTO> bookingDTOList   = bookingService.getBookingList();
        System.out.println("после выполнения");
        //System.out.println("Class: " + bookingDTOList.get(0).getClass());
        return ResponseEntity.ok(bookingDTOList);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("my")
    public ResponseEntity<?> getBookingsMy() {
        List<BookingDTO> bookingListMy   = bookingService.getBookingListMy();
        if (bookingListMy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have any bookings!");
        }
        return ResponseEntity.ok(bookingListMy);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("my/from={dateFrom}&to={dateTo}")
    public ResponseEntity<?> getBookingsMyRangeDate(@PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        List<BookingDTO> bookingListMy   = bookingService.getMyBookingDateRange(dateFrom,dateTo);
        if (bookingListMy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have any bookings!");
        }
        return ResponseEntity.ok(bookingListMy);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("edit_booking/{id}")
    public ResponseEntity<?> editBooking(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Booking booking = bookingService.findByIdBooking(id);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found");
        } else {
            bookingService.updateBookingPartial(id,updates);
        }
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("add_booking")
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) {
        Boolean saveBooking   = bookingService.saveBookingEmailConfirmation(booking);
        if (saveBooking == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dates are busy");
        }
        return ResponseEntity.ok("Booking was added");
    }

    @GetMapping("hello")
    public void helloW() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();
            System.out.println(userN);
//            emailService.sendSimpleEmail(
//                    userN.getEmail(),
//                    "Confirming booking at hotel Lox ",
//                    "Thank you"
//            );

        }
//        Room room = roomService.findRoomWithHotel(booking.getRoom().getId()).orElse(null);
//        System.out.println("hotel name ---------------" + room.getHotel().getName());

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("update_booking")
    public String updateBooking(@RequestBody Booking booking) {
        bookingService.updateBooking(booking);
        return "Booking was updated";
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("update_booking/{id}")
//    public String updateBookingPartial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//        bookingService.updateBookingPartial(id,updates);
//        return "Booking was updated";
//    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("delete_booking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }
}
