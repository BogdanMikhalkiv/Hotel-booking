package com.example.hotelbookingsystem.repository;


import com.example.hotelbookingsystem.Models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Hotel findAllById(Long id);
}
