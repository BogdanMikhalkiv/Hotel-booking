package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.AuditLog;
import com.example.hotelbookingsystem.repository.AuditLogRepository;
import com.example.hotelbookingsystem.service.AuditLogService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@AllArgsConstructor
@Primary
public class AuditLogServiceImpl implements AuditLogService {


    private AuditLogRepository auditLogRepository;

    @Override
    public void logAction(AuditLog auditLog) {
         auditLogRepository.save(auditLog);
    }
}
