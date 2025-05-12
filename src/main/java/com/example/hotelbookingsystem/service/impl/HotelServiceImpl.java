package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.Hotel;
import com.example.hotelbookingsystem.repository.HotelRepository;
import com.example.hotelbookingsystem.service.HotelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class HotelServiceImpl  implements HotelService {

    private final HotelRepository hotelRepository;



    @Override
    public List<Hotel> getHotelList() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel findByIdHotel(Long id) {
        return hotelRepository.findAllById(id);
    }

    @Override
    public Hotel updateHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }


}
