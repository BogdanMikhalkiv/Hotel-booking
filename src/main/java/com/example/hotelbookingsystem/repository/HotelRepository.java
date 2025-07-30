package com.example.hotelbookingsystem.repository;


import com.example.hotelbookingsystem.Models.DTO.HotelDTO;
import com.example.hotelbookingsystem.Models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findAllById(Long id);

    @Query(
                    " select new com.example.hotelbookingsystem.Models.DTO.HotelDTO(h.id,h.name,h.telefon,h.street,h.rating) " +
                    " from Hotel h"
    )
    List<HotelDTO> findAllHotels();
}
