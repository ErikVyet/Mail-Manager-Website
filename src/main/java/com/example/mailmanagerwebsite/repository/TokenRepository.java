package com.example.mailmanagerwebsite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findById(int id);
    Optional<Token> findByValue(String value);
    Optional<Token> findByNameAndUserId(String name, int userId);
    
}