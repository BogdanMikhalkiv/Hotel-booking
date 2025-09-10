package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.*;
import com.example.hotelbookingsystem.Models.DTO.RoomDTO;
import com.example.hotelbookingsystem.repository.ActionTypeRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.HotelService;
import com.example.hotelbookingsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class RoomServiceImpl implements RoomService {
    private  RoomRepository roomRepository;
    private AuditLogServiceImpl auditLogService;
    private ActionTypeRepository actionTypeRepository;
    private HotelService hotelService;

    @Override
    @Cacheable(value = "room")
    public List<Room> getRoomList() {
        System.out.println("11");

        return roomRepository.findAll();
    }

    @Override
    @CacheEvict(value = "room", allEntries = true)
    public Room saveRoom(RoomDTO room) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //System.out.println(room.getHotel().getId() + " =======================");
        Hotel hotel = hotelService.findByIdHotel(room.getHotelId());
        Room roomObject = new Room();
        roomObject.setHotel(hotel);
        roomObject.setPrice(room.getPrice());
        roomObject.setCapacity(room.getCapacity());
        ActionType actionType = actionTypeRepository.findByName("Create");

        roomRepository.save(roomObject);
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(roomObject.getId())
                        .entityType(Room.class.getSimpleName())
                        .actionType(actionType)
                        .build()
        );
        return roomObject;
    }

    @Override
    @Cacheable(value = "room", key = "#id")
    public Optional<Room> findByIdRoom(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<Room> room = roomRepository.findById(id);

        room.ifPresent(r -> auditLogService.logAction(
                AuditLog.builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(r.getId())
                        .entityType(Room.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Read"))
                        .build()
        ));
        return room;
    }

    @Override
    @Cacheable(value = "room", key = "#id")
    public Optional<Room> findRoomWithHotel(Long id) {
        return roomRepository.findRoomWithHotel(id);
    }

    @Override
    @CachePut(value = "room", key = "#id")
    @CacheEvict(value = "room", allEntries = true)
    public Room updateRoomPartial(Long id, Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Room room = roomRepository.findById(id) .orElseThrow(() -> new RuntimeException("Room not found"));;

        if (room == null){
            return null;
        }else {
            if (updates.containsKey("price")) {
                room.setPrice(Double.valueOf( updates.get("price").toString()));
            }
            if (updates.containsKey("capacity")) {
                room.setCapacity(Integer.valueOf( updates.get("capacity").toString()));
            }
            if (updates.containsKey("hotelId")) {
                room.setHotel(hotelService.findByIdHotel(Long.parseLong(updates.get("hotelId").toString())));
            }

            System.out.println(actionTypeRepository.findByName("Update"));
            //System.out.println((UserN) auth.getPrincipal()+ "------------------------------------------------------------------");

            AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .userN((UserN) auth.getPrincipal())
                    .entityId(room.getId())
                    .entityType(Room.class.getSimpleName())
                    .actionType(actionTypeRepository.findByName("Update"))
                    .build();
            roomRepository.save(room);
            return room;
        }
    }

    @Override
    @CachePut(value = "room", key = "#room.id")
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @CacheEvict(value = "room", allEntries = true)
    public Room deleteRoom(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Room> room = roomRepository.findById(id);

        if (!room.isPresent()){
            return null;
        }else {
            auditLogService.logAction(
                    AuditLog.builder()
                            .timestamp(LocalDateTime.now())
                            .userN((UserN) auth.getPrincipal())
                            .entityId(room.get().getId())
                            .entityType(Room.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Delete"))
                            .build()
            );

            roomRepository.deleteById(id);
            return room.get();
        }
    }
}
