package com.example.hotelbookingsystem.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Booking")
public class Booking implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "userN_id", nullable = false)
    private UserN userN;

}
