package com.example.hotelbookingsystem.Controllers;


import com.example.hotelbookingsystem.Models.AuditLog;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.Models.DTO.HotelDTO;
import com.example.hotelbookingsystem.Models.Hotel;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.service.AuditLogService;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.HotelService;
import com.example.hotelbookingsystem.service.impl.AuditLogServiceImpl;
import com.example.hotelbookingsystem.service.impl.HotelServiceImpl;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/hotel")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<?> getHotels() {
        return ResponseEntity.ok(hotelService.getHotelList());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getHotelById(@PathVariable() Long id) {

        return ResponseEntity.ok(hotelService.getHotelById( id));
    }

    @PostMapping("add_hotel")
    public String saveHotel(@RequestBody Hotel hotel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(hotel);
        hotelService.saveHotel(hotel);

        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(hotel.getId())
                        .entityType(Hotel.class.getName())
                        .action("add Hotel")
                        .build()


        );
        return "Hotel was added";
    }

    @PutMapping("update_hotel")
    public String updateHotel(@RequestBody Hotel hotel) {
        hotelService.updateHotel(hotel);
        return "Hotel was updated";
    }

    @DeleteMapping("delete_hotel/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);

        return "hotel was deleted by id - " + id;
    }
}
