package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.DTO.RoomDTO;
import com.example.hotelbookingsystem.Models.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoomService {
    List<Room> getRoomList();
    Room saveRoom(RoomDTO room);
    Optional<Room> findByIdRoom (Long id);
    Optional<Room> findRoomWithHotel(Long id);
    Room updateRoomPartial(Long id, Map<String, Object> updates);
    Room updateRoom(Room room);
    Room deleteRoom(Long id);
}
