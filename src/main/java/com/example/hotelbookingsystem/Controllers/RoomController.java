package com.example.hotelbookingsystem.Controllers;

import com.example.hotelbookingsystem.Models.DTO.RoomDTO;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/room")
@AllArgsConstructor
public class RoomController {

    private RoomService roomService;

    @GetMapping
    public List<RoomDTO> getRooms() {
        return roomService.getRoomList().stream()
                .map(room -> new RoomDTO(
                        room.getId(),
                        room.getPrice(),
                        room.getCapacity(),
                        room.getHotel().getId(),
                        room.getHotel().getName()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public Optional<RoomDTO> getRoomsById(@PathVariable Long id) {
        return roomService.findByIdRoom(id).map(room -> new RoomDTO(
                room.getId(),
                room.getPrice(),
                room.getCapacity(),
                room.getHotel().getId(),
                room.getHotel().getName()
        ));
    }

    @PostMapping("add_room")
    public String saveRoom(@RequestBody Room room) {
        System.out.println("зашел в addRoom =============");
        System.out.println(room + "==================");
        roomService.saveRoom(room);
        return "room was added";
    }

    @PutMapping("update_room")
    public String updateRoom(@RequestBody Room room) {
        roomService.updateRoom(room);
        return "room was updated";
    }

    @DeleteMapping("delete_room/{id}")
    public String deleteRoom(@PathVariable Long id) {
        System.out.println(id + "-------------------------");
        roomService.deleteRoom(id);
        return "room was deleted by id - " + id;
    }
}
