package com.example.mailmanagerwebsite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Email;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    public Optional<Email> findById(int id);
    
}