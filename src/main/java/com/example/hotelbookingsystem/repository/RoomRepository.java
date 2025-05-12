package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room>  findAllById(Long id);
}
