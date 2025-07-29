package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
