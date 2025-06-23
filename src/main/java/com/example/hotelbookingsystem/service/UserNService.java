package com.example.hotelbookingsystem.service;

import com.example.hotelbookingsystem.Models.UserN;

import java.util.List;
import java.util.Optional;

public interface UserNService {

    List<UserN> getUserList();
    UserN saveUser(UserN userN);
    UserN findByIdUser(Long id);
    UserN updateUser(UserN userN);
    void deleteUser(Long id);

}
