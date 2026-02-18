package com.example.mailmanagerwebsite.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.model.User;
import com.example.mailmanagerwebsite.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> getUser(String vmail, String password) {
        return this.repository.findByVmailAndPassword(vmail, password);
    }

    @Transactional
    public boolean registerUser(User user) {
        if (this.repository.findByVmail(user.getVmail()).isPresent()) {
            return false;
        }
        if (this.repository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        this.repository.save(user);
        return true;
    }

    @Transactional
    public boolean resetPassword(String vmail, String password) {
        Optional<User> opt = this.repository.findByVmail(vmail);
        if (opt.isPresent()) {
            User user = opt.get();
            user.setPassword(password);
            this.repository.save(user);
            return true;
        }
        return false;
    }

}