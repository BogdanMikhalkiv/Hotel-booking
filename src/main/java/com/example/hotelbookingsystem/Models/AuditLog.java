package com.example.hotelbookingsystem.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AuditLog")
public class AuditLog implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String entityType;
    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "userN_id", nullable = false)
    private UserN userN;

    @ManyToOne
    @JoinColumn(name = "actionType_id", nullable = false)
    private ActionType actionType;
}
