package com.example.hotelbookingsystem.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO implements Serializable {
    private Long id;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Long roomId;
    private Long userId;
}
