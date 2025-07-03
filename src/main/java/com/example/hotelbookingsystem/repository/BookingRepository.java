package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.Room;
import com.example.hotelbookingsystem.Models.UserN;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    Booking findAllById(Long id);

    @Query(
            " select b" +
            " from Booking b" +
            " join fetch b.room r" +
            " join fetch r.hotel h" +
            " join fetch b.userN u" +
            " where b.userN = :userN"
    )
    List<Booking> findBookingByUserN(@Param("userN")UserN userN);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            " select b" +
            " from Booking b" +
            " where (:NEW_DATE_FROM >= b.dateFrom AND :NEW_DATE_FROM < b.dateTo) " +
            " or (:NEW_DATE_TO > b.dateFrom AND :NEW_DATE_TO <= b.dateTo)" +
            " and b.room.id = :ROOM_ID"
    )
    List<Booking> findRoomsByID(@Param("NEW_DATE_FROM") LocalDate NEW_DATE_FROM,
                             @Param("NEW_DATE_TO") LocalDate NEW_DATE_TO,
                            @Param("ROOM_ID") Long ROOM_ID);
}
