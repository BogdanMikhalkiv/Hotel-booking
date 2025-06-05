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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final BookingService bookingService;
    private  final UserNService userNService;
    private final RoomService roomService;
    private final EmailService emailService;
    private final HotelService hotelService;


    @GetMapping
    public List<Booking> getBookings() {
        return bookingService.getBookingList();
    }

    @GetMapping("my")
    public ResponseEntity<?> getBookingsMy() {

        System.out.println("getBookingsMy");
        List<BookingDTO> bookingListMy   = bookingService.getBookingListMy().stream().map(booking -> new BookingDTO(
                booking.getId(),
                booking.getDateFrom(),
                booking.getDateTo(),
                booking.getRoom().getId(),
                booking.getUserN().getId()
        )).toList();
        if (bookingListMy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have any bookings!");
        }
        return ResponseEntity.ok(bookingListMy);
    }
    @GetMapping("my/from={dateFrom}&to={dateTo}")
    public ResponseEntity<?> getBookingsMyRangeDate(@PathVariable LocalDate dateFrom, @PathVariable LocalDate dateTo) {
        System.out.println("getBookingsMyRangeDate");
        System.out.println(dateFrom + "-------- " + dateTo);
        List<BookingDTO> bookingListMy   = bookingService.getBookingListMy()
                                            .stream()
                                            .filter(b -> b.getDateFrom().isAfter(dateFrom.minusDays(1))
                                                    &&  b.getDateTo().isBefore(dateTo.plusDays(1))
                                            )
                                            .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getDateFrom(),
                        booking.getDateTo(),
                        booking.getRoom().getId(),
                        booking.getUserN().getId()
        )).toList();
        System.out.println(bookingListMy);
        if (bookingListMy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You do not have any bookings!");
        }
        return ResponseEntity.ok(bookingListMy);
    }

    @PatchMapping("edit_booking/{id}")
    public ResponseEntity<?> editBooking(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Booking booking = bookingService.findByIdBooking(id);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found");
        }else {
            if (updates.containsKey("dateFrom")) {
                booking.setDateFrom(LocalDate.parse(String.valueOf(updates.get("dateFrom"))));
            }
            if (updates.containsKey("dateTo")) {
                booking.setDateTo(LocalDate.parse(String.valueOf(updates.get("dateTo"))));
            }
            if (updates.containsKey("userNId")) {
                booking.setUserN(userNService.findByIdUser(Long.valueOf( updates.get("userNId").toString())));
            }
            if (updates.containsKey("roomId")) {
                booking.setRoom(roomService.findByIdRoom((Long) updates.get("roomId")).orElse(null));
            }
        }
        return ResponseEntity.ok(booking);
    }


        @PostMapping("add_booking")
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) {
        Boolean saveBooking   = bookingService.saveBooking(booking);

        if (saveBooking == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dates are busy");
        }

        return ResponseEntity.ok("Booking was added");
    }

    @GetMapping("hello")
    public void helloW(@RequestBody Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();
            emailService.sendSimpleEmail(
                    userN.getEmail(),
                    "Confirming booking at hotel Lox ",
                    "Thank you"
            );
        }


        Room room = roomService.findRoomWithHotel(booking.getRoom().getId()).orElse(null);
        System.out.println("hotel name ---------------" + room.getHotel().getName());

    }



    @PutMapping("update_booking")
    public String updateBooking(@RequestBody Booking booking) {
        bookingService.updateBooking(booking);
        return "Booking was updated";
    }

    @DeleteMapping("delete_booking/{id}")
    public String deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }
}
