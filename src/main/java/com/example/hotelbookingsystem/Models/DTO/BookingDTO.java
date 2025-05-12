package com.example.hotelbookingsystem.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long roomId;
    private Long userId;
}
