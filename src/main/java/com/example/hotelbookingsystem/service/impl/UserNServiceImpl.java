package com.example.hotelbookingsystem.service.impl;

import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.repository.UserRepository;
import com.example.hotelbookingsystem.service.UserNService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "userN")
    public List<UserN> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public UserN saveUser(UserN userN) {
        return null;
    }


    @Override
    @Cacheable(value = "userN",key = "#id")
    public UserN findByIdUser(Long id) {
        return userRepository.findAllById(id);
    }

    @Override
    @CachePut(value = "userN",key = "#userN.id")
    public UserN updateUser(UserN userN) {
        return userRepository.save(userN);
    }



    @Override
    @CacheEvict(value = "userN",key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
