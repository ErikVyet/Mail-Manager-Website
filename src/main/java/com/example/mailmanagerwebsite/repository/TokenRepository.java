package com.example.mailmanagerwebsite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByValue(String value);
}