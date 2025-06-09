package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class RoomServiceImpl implements RoomService {
    private  RoomRepository roomRepository;

    @Override
    @Cacheable(value = "room")
    public List<Room> getRoomList() {
        System.out.println("11");
        return roomRepository.findAll();
    }

    @Override
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @Cacheable(value = "room", key = "#id")
    public Optional<Room> findByIdRoom(Long id) {
        return roomRepository.findAllById(id);
    }

    @Override
    @Cacheable(value = "room", key = "#id")
    public Optional<Room> findRoomWithHotel(Long id) {
        return roomRepository.findRoomWithHotel(id);
    }

    @Override
    @CachePut(value = "room", key = "#room.id")
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @CacheEvict(value = "room", key = "#id")
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
