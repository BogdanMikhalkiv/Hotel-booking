package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.*;
import com.example.hotelbookingsystem.Models.DTO.BookingDTO;
import com.example.hotelbookingsystem.repository.ActionTypeRepository;
import com.example.hotelbookingsystem.repository.BookingRepository;
import com.example.hotelbookingsystem.repository.RoomRepository;
import com.example.hotelbookingsystem.service.AuditLogService;
import com.example.hotelbookingsystem.service.BookingService;
import com.example.hotelbookingsystem.service.RoomService;
import com.example.hotelbookingsystem.service.UserNService;
import com.example.hotelbookingsystem.service.emailService.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
@Primary
public class BookingServiceImpl implements BookingService {

    private  BookingRepository bookingRepository;
    private final RoomService roomService;
    private final EmailService emailService;
    private final UserNService userNService;
    private final AuditLogService auditLogService;
    private final ActionTypeRepository actionTypeRepository;



//    @Override
//    @Cacheable(value = "booking")
//    public List<Booking> getBookingList() {
//        return bookingRepository.findAll();
//
//    }
    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "booking")
    public List<BookingDTO> getBookingList() {
        System.out.println("getBookingList");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BookingDTO> bookingDTOList = bookingRepository.findAllBooking()
                .stream()
                .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getDateFrom(),
                        booking.getDateTo(),
                        booking.getRoom().getId(),
                        booking.getUserN().getId()
                )).toList();
//        auditLogService.logAction(
//                AuditLog
//                        .builder()
//                        .timestamp(LocalDateTime.now())
//                        .userN((UserN) auth.getPrincipal())
//                        .entityType(Booking.class.getSimpleName())
//                        .actionType(actionTypeRepository.findByName("Read"))
//                        .build()
//        );
        return bookingDTOList;
    }

    @Override
    @Cacheable(value = "bookingMy")
    public List<BookingDTO> getBookingListMy() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();

            auditLogService.logAction(
                    AuditLog
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .userN(userN)
                            .entityType(Booking.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Read"))
                            .build()
            );

            return  bookingRepository.findBookingByUserN(userN)
                                                                .stream()
                                                                .map(booking -> new BookingDTO(
                                                                        booking.getId(),
                                                                        booking.getDateFrom(),
                                                                        booking.getDateTo(),
                                                                        booking.getRoom().getId(),
                                                                        booking.getUserN().getId()
                                                                )).toList();
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "booking")
    public List<BookingDTO> getMyBookingDateRange(LocalDate dateFrom, LocalDate dateTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<BookingDTO> bookingListMy   = getBookingListMy()
                                                            .stream()
                                                            .filter(b -> b.getDateFrom().isAfter(dateFrom.minusDays(1))
                                                                    &&   b.getDateTo().isBefore(dateTo.plusDays(1))
                                                            ).toList();
        auditLogService.logAction(
                AuditLog
                        .builder()
                        .timestamp(LocalDateTime.now())
                        .userN((UserN) auth.getPrincipal())
                        .entityType(Booking.class.getSimpleName())
                        .actionType(actionTypeRepository.findByName("Read"))
                        .build()
        );

        return bookingListMy;
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    @CacheEvict(value = "booking", allEntries = true)
    public Boolean saveBookingEmailConfirmation(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Checking if a room is available in that range of dates
        if (
                bookingRepository.findRoomsByID(
                                    booking.getDateFrom(),
                                    booking.getDateTo(),
                                    booking.getRoom().getId()
            ).isEmpty()) {
            bookingRepository.save(booking);

            //email information preparation
            Room room = roomService.findRoomWithHotel(booking.getRoom().getId()).orElse(null);
            String nameHotel = room.getHotel().getName();
            String msg = "Hello ,\n thank you for booking a room, starts at "
                    + booking.getDateFrom()
                    + ", ends - " + booking.getDateTo();

            //sending email
            if (!(auth instanceof AnonymousAuthenticationToken)) {
                UserN userN = (UserN) auth.getPrincipal();
                emailService.sendSimpleEmail(
                        userN.getEmail(),
                        "Confirming booking at hotel " + nameHotel,
                        msg
                );
            }

            // log add
            auditLogService.logAction(
                    AuditLog
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .userN((UserN) auth.getPrincipal())
                            .entityId(booking.getId())
                            .entityType(Booking.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Create"))
                            .build()
            );
            return  true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    @CacheEvict(value = "booking", allEntries = true)
    public Boolean saveBooking(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (
                bookingRepository.findRoomsByID(
                        booking.getDateFrom(),
                        booking.getDateTo(),
                        booking.getRoom().getId()
                ).isEmpty()) {
            bookingRepository.save(booking);
            auditLogService.logAction(
                    AuditLog
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .userN((UserN) auth.getPrincipal())
                            .entityId(booking.getId())
                            .entityType(Booking.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Create"))
                            .build()
            );
            return  true;
        }
        return false;
    }



    @Override
    @Cacheable(value = "booking",key = "#id")
    public Booking findByIdBooking(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Booking booking = bookingRepository.findAllById(id);

        if (booking == null) {
            return null;
        } else {

            auditLogService.logAction(
                    AuditLog
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .userN((UserN) auth.getPrincipal())
                            .entityId(booking.getId())
                            .entityType(Booking.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Read"))
                            .build()
            );
            return booking;

        }

    }

    @Override
    @CachePut(value = "booking",key = "#booking.id")
    public Booking updateBooking(Booking booking) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserN userN = (UserN) auth.getPrincipal();
            if (userN.getRole().equals("ROLE_USER")) {
                if (!bookingRepository.findBookingByUserNAndId(userN,booking.getId()).isEmpty()) {
                    bookingRepository.save(booking);
                }
            }
        }

        return bookingRepository.save(booking);
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    @CachePut(value = "booking", key = "#result.id")
    public Booking updateBookingPartial(Long id,  Map<String, Object> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Booking booking = bookingRepository.findAllById(id);
        if (booking == null) {
             return null;
        } else {

            if (updates.containsKey("dateFrom")) {
                booking.setDateFrom(LocalDate.parse(String.valueOf(updates.get("dateFrom"))));
            }
            if (updates.containsKey("dateTo")) {
                booking.setDateTo(LocalDate.parse(String.valueOf(updates.get("dateTo"))));
            }
            if (updates.containsKey("userNId")) {
                booking.setUserN(userNService.findByIdUser(Long.valueOf(updates.get("userNId").toString())));
            }
            if (updates.containsKey("roomId")) {
                booking.setRoom(roomService.findByIdRoom((Long) updates.get("roomId")).orElse(null));
            }


            if (
                    bookingRepository.findRoomsByIDEdit(
                            booking.getDateFrom(),
                            booking.getDateTo(),
                            booking.getRoom().getId(),
                            booking.getId()
                    ).isEmpty()) {
                bookingRepository.save(booking);
            }

            auditLogService.logAction(
                    AuditLog
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .userN((UserN) auth.getPrincipal())
                            .entityId(booking.getId())
                            .entityType(Booking.class.getSimpleName())
                            .actionType(actionTypeRepository.findByName("Update"))
                            .build()
            );
            return booking;
        }
    }

    @Override
    @CacheEvict(value = "booking",key = "#id")
    public String deleteBooking(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Booking bookingToDelete = bookingRepository.findAllById(id);

        if (bookingToDelete == null) {
            return "no such booking with Id - " +  id;
        } else {

            if (!LocalDate.now().isBefore( bookingToDelete.getDateFrom())) {
                return "You can't anymore cancel your booking";
            } else {


                auditLogService.logAction(
                        AuditLog
                                .builder()
                                .timestamp(LocalDateTime.now())
                                .userN((UserN) auth.getPrincipal())
                                .entityId(bookingToDelete.getId())
                                .entityType(Booking.class.getSimpleName())
                                .actionType(actionTypeRepository.findByName("Delete"))
                                .build()
                );
                bookingRepository.deleteById(id);
                return "Booking  was canceled  with id = " + id;
            }
        }


    }
}
