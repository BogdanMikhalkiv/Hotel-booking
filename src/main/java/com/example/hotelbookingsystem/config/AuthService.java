package com.example.hotelbookingsystem.config;


import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;





    public String register(UserN user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);
        return "user was registered" + user;
    }

    public Optional<UserN> findByEmail(String email) {
       return userRepository.findByEmail(email);
    }
}
