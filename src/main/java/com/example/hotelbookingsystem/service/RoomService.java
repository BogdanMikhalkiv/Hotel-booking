package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getRoomList();
    Room saveRoom(Room room);
    Optional<Room> findByIdRoom (Long id);
    Room updateRoom(Room room);
    void deleteRoom(Long id);
}
