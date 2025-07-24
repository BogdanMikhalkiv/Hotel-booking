package com.example.hotelbookingsystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Hotel")
public class Hotel implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String telefon;
    private String street;
    private Double rating;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Room> rooms;
}

