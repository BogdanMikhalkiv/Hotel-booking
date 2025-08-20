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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
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

        if (hotelService.findByIdHotel( id) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found");
        }

        return ResponseEntity.ok(hotelService.findByIdHotel(id));
    }

    @GetMapping("/clear")
    @CacheEvict(value = "hotel", allEntries = true)
    public void clearHotelCache() {
        System.out.println("Кэш очищен");
    }

    @PostMapping("add_hotel")
    public String saveHotel(@RequestBody Hotel hotel) {
        System.out.println(hotel);
        hotelService.saveHotel(hotel);

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
