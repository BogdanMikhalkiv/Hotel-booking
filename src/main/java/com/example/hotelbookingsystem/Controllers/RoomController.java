package com.example.hotelbookingsystem.Controllers;

import com.example.hotelbookingsystem.Models.AuditLog;
import com.example.hotelbookingsystem.Models.DTO.RoomDTO;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.ActionTypeRepository;
import com.example.hotelbookingsystem.service.AuditLogService;
import com.example.hotelbookingsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/room")
@AllArgsConstructor
public class RoomController {

    private RoomService roomService;
    private AuditLogService auditLogService;
    private ActionTypeRepository actionTypeRepository;

    @GetMapping
    public List<RoomDTO> getRooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityType(Room.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Read"))
                        .build()
        );

        List<RoomDTO> roomDTOS = roomService.getRoomList().stream()
                .map(room -> new RoomDTO(
                        room.getId(),
                        room.getPrice(),
                        room.getCapacity(),
                        room.getHotel().getId(),
                        room.getHotel().getName()
                ))
                .toList();

        return roomDTOS;
    }

    @GetMapping("/clear")
    @CacheEvict(value = "room", allEntries = true)
    public void clearRoomCache() {
        System.out.println("Кэш очищен");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomsById(@PathVariable Long id) {
        Optional<Room> room = roomService.findByIdRoom(id);

       if (!room.isPresent()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found");
       }
       RoomDTO roomDTO = new RoomDTO(
               room.get().getId(),
               room.get().getPrice(),
               room.get().getCapacity(),
               room.get().getHotel().getId(),
               room.get().getHotel().getName()
       );
       return ResponseEntity.ok(roomDTO);


    }

    @PostMapping("add_room")
    public String saveRoom(@RequestBody RoomDTO room) {

        roomService.saveRoom(room);
        return "room was added";
    }

    @PutMapping("update_room")
    public String updateRoom(@RequestBody Room room) {
        roomService.updateRoom(room);
        return "room was updated";
    }

    @PatchMapping("edit_room/{id}")
    public ResponseEntity<?> editRoomPartial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Room room = roomService.updateRoomPartial(id,updates);
        System.out.println(room);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found room");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("room was edited with Id - " + room.getId());
        }
    }

    @DeleteMapping("delete_room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        Room room = roomService.deleteRoom(id);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not found room");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("room was deleted with Id - " + room.getId());
        }
    }
}
