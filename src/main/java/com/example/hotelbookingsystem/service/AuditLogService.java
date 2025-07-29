package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.AuditLog;

import java.time.LocalDateTime;

public interface AuditLogService {

    void logAction(AuditLog auditLog);
}
