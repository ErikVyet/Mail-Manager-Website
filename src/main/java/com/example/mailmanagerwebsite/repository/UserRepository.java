package com.example.mailmanagerwebsite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByPhone(String phone);
    public Optional<User> findByVmail(String vmail);
    public Optional<User> findByUsername(String username);
    public Optional<User> findByVmailAndPassword(String vmail, String password);

}