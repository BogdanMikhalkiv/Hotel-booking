package com.example.hotelbookingsystem.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO  implements Serializable {

    private Long id;
    private String name;
    private String telefon;
    private String street;
    private Double rating;

}
