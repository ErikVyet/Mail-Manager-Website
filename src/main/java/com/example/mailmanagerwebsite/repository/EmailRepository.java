package com.example.mailmanagerwebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Email;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    
    
}