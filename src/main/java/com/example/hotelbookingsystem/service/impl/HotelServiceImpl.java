package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.AuditLog;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.Models.DTO.HotelDTO;
import com.example.hotelbookingsystem.Models.Hotel;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.ActionTypeRepository;
import com.example.hotelbookingsystem.repository.HotelRepository;
import com.example.hotelbookingsystem.service.AuditLogService;
import com.example.hotelbookingsystem.service.HotelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class HotelServiceImpl  implements HotelService {

    private final HotelRepository hotelRepository;
    private AuditLogService auditLogService;
    private ActionTypeRepository actionTypeRepository;


    @Override
    @Cacheable(value = "hotel")
    public List<HotelDTO> getHotelList() {
        System.out.println("getHotelList");
        List<HotelDTO> hotelDTOS = hotelRepository.findAllHotels();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityType(Hotel.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Read"))
                        .build()
        );
        return hotelDTOS;
    }

    @Override
    public Hotel getHotelById(Long id ) {
        System.out.println("getHotelById");
        Hotel  hotel = hotelRepository.findAllById(id);

        return hotel;
    }

    @Override
    @CacheEvict(value = "hotel", allEntries = true)
    public Hotel saveHotel(Hotel hotel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        hotelRepository.save(hotel);
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(hotel.getId())
                        .entityType(Hotel.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Create"))
                        .build()
        );

        return hotel;
    }

    @Override
    @Cacheable(value = "hotel", key = "#id")
    public Hotel findByIdHotel(Long id) {
        Hotel  hotel = hotelRepository.findAllById(id);

        if (hotel == null){
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(hotel.getId())
                        .entityType(Hotel.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Read"))
                        .build()
        );
        return hotel;
    }

    @Override
    @CachePut(value = "hotel", key = "#result.id")
    public Hotel updateHotel(Hotel hotel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(hotel.getId())
                        .entityType(Hotel.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Update"))
                        .build()
        );
        return hotelRepository.save(hotel);
    }

    @Override
    @CacheEvict(value = "hotel", key = "#id")
    public Hotel deleteHotel(Long id) {
        Hotel hotel = findByIdHotel(id);
        if (hotel == null){
            return null;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityId(hotel.getId())
                        .entityType(Hotel.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Delete"))
                        .build()
        );
        hotelRepository.deleteById(hotel.getId());
        return hotel;
    }


}
