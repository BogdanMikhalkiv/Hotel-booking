package com.example.hotelbookingsystem.Controllers;


import com.example.hotelbookingsystem.Models.Hotel;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.HotelService;
import com.example.hotelbookingsystem.service.impl.HotelServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/hotel")
@AllArgsConstructor
public class HotelController {


    private final HotelService hotelService;


//    public HotelController(HotelService hotelService) {
//        this.hotelService = hotelService;
//    }


    @GetMapping
    public List<Hotel> getHotels() {
        return hotelService.getHotelList();
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
