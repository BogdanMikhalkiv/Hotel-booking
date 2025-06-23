package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.Hotel;

import java.util.List;

public interface HotelService {

    List<Hotel> getHotelList();
    Hotel saveHotel(Hotel hotel);
    Hotel findByIdHotel (Long id);
    Hotel updateHotel(Hotel hotel);
    void deleteHotel(Long id);

}
