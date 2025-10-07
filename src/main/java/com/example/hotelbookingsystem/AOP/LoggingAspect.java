package com.example.hotelbookingsystem.AOP;

import com.example.hotelbookingsystem.Controllers.BookingController;
import com.example.hotelbookingsystem.Models.AuditLog;
import com.example.hotelbookingsystem.Models.Booking;
import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.ActionTypeRepository;
import com.example.hotelbookingsystem.service.AuditLogService;
import com.example.hotelbookingsystem.service.UserNService;
import com.example.hotelbookingsystem.service.emailService.EmailService;
import lombok.AllArgsConstructor;
import org.apache.catalina.Session;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private AuditLogService auditLogService ;
    private ActionTypeRepository actionTypeRepository;
    private static  final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(
            "@within(org.springframework.web.bind.annotation.RestController) && " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)"
             )
    public void isGetMappingInController() {

    }

    @AfterReturning("isGetMappingInController()")
    public void  method() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditLogService.logAction(
                AuditLog
                    .builder()
                    .timestamp(LocalDateTime.now())
                    .userN((UserN) auth.getPrincipal())
                    .entityType(Booking.class.getSimpleName())
                    .actionType(actionTypeRepository.findByName("Read"))
                    .build()
        );
        logger.info("============ After =================");

    }

}
