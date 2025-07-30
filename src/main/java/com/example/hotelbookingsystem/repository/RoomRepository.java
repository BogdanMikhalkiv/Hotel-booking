package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room>  findAllById(Long id);

    @Query("SELECT r FROM Room r JOIN FETCH r.hotel WHERE r.id = :id")
    Optional<Room> findRoomWithHotel(@Param("id") Long id);


}
