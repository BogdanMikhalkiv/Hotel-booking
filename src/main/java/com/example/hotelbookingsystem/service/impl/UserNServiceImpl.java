package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.UserRepository;
import com.example.hotelbookingsystem.service.UserNService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class UserNServiceImpl implements UserNService {

    private UserRepository userRepository;

    @Override
    public List<UserN> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public UserN saveUser(UserN userN) {
        return null;
    }


    @Override
    public UserN findByIdUser(Long id) {
        return userRepository.findAllById(id);
    }

    @Override
    public UserN updateUser(UserN userN) {
        return userRepository.save(userN);
    }



    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
