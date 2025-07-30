package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.Models.DTO.HotelDTO;
import com.example.hotelbookingsystem.Models.Hotel;
import com.example.hotelbookingsystem.repository.HotelRepository;
import com.example.hotelbookingsystem.service.HotelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class HotelServiceImpl  implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    //@Cacheable(value = "hotel")
    public List<HotelDTO> getHotelList() {
        System.out.println("getHotelList");
        List<HotelDTO> hotelDTOS = hotelRepository.findAllHotels();
//                .stream()
//                .map(hotel -> new HotelDTO(
//                        hotel.getId(),
//                        hotel.getName(),
//                        hotel.getTelefon(),
//                        hotel.getStreet(),
//                        hotel.getRating()
//                )).toList();
        return hotelDTOS;
    }

    @Override
    public Hotel getHotelById(Long id ) {
        System.out.println("getHotelById");
        Hotel  hotel = hotelRepository.findAllById(id);
        return hotel;
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @Cacheable(value = "hotel", key = "#id")
    public Hotel findByIdHotel(Long id) {
        return hotelRepository.findAllById(id);
    }

    @Override
    @CachePut(value = "hotel", key = "#hotel.id")
    public Hotel updateHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @CacheEvict(value = "hotel", key = "#id")
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }


}
