package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.UserN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserN, Long> {

    Optional<UserN> findByEmail(String email);

    UserN findAllById(Long id);

}
