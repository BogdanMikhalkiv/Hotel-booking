package com.example.hotelbookingsystem.config;


import com.example.hotelbookingsystem.Models.UserN;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserN user) {
        return ResponseEntity.ok(service.register(user));
    }
    @GetMapping("/whoami")
    public ResponseEntity<String> whoAmI(@RequestHeader Map<String, String> headers,
                                         @RequestBody AuthRequest authRequest) {
        System.out.println(headers);
        var user = UserN.builder()
                    .email(authRequest.getEmail())
                    .password(authRequest.getPassword())
                    .build();
        return ResponseEntity.ok("Ваш email: " + user.getEmail());
    }

}
