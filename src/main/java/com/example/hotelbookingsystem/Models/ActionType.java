package com.example.hotelbookingsystem.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ActionType")
public class ActionType {
    @Id
    @GeneratedValue
    private Long id;

    private String actionName;

    @OneToMany(mappedBy = "actionType",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AuditLog> auditLogs;

}
