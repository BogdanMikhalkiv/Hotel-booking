package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.DTO.HotelDTO;
import com.example.hotelbookingsystem.Models.Hotel;

import java.util.List;

public interface HotelService {

    List<HotelDTO> getHotelList();
    Hotel getHotelById(Long id);


    Hotel saveHotel(Hotel hotel);
    Hotel findByIdHotel (Long id);
    Hotel updateHotel(Hotel hotel);
    void deleteHotel(Long id);

}
