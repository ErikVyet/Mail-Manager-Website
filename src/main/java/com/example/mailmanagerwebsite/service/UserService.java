package com.example.mailmanagerwebsite.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.dto.UserLoginRequest;
import com.example.mailmanagerwebsite.dto.UserRegisterRequest;
import com.example.mailmanagerwebsite.model.User;
import com.example.mailmanagerwebsite.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<UserDTO> getUser(UserLoginRequest req) {
        req.setPassword(DigestUtils.sha256Hex(req.getVmail() + req.getPassword()));
        Optional<User> opt = repository.findByVmailAndPassword(req.getVmail(), req.getPassword());
        Optional<UserDTO> optDTO = Optional.empty();
        if (opt.isPresent()) {
            User user = opt.get();
            user.setLogin(LocalDateTime.now());
            this.repository.save(user);
            optDTO = Optional.of(UserDTO.convert(user));
        }
        return optDTO;
    }

    @Transactional
    public Optional<UserDTO> createUser(UserRegisterRequest req) {
        if (req.getPassword().equals(req.getConfirmPassword())) {
            req.setPassword(DigestUtils.sha256Hex(req.getVmail() + req.getPassword()));
            Optional<User> opt = repository.findByVmail(req.getVmail());
            if (opt.isPresent()) {
                return Optional.empty();
            }
            opt = repository.findByUsername(req.getUsername());
            if (opt.isPresent()) {
                return Optional.empty();
            }
            User user = new User();
            user.setVmail(req.getVmail());
            user.setPassword(req.getPassword());
            user.setFullname(req.getFullname());
            user.setLogin(LocalDateTime.now());
            this.repository.save(user);
            return Optional.of(UserDTO.convert(user));
        }
        return Optional.empty();
    }

}