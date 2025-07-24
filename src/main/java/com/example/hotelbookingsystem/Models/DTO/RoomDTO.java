package com.example.hotelbookingsystem.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO implements Serializable {
    private Long id;
    private Double price;
    private Integer capacity;
    private Long hotelId;
    private String hotelName;
}
