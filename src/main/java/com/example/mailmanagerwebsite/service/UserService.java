package com.example.mailmanagerwebsite.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.dto.UserLoginRequest;
import com.example.mailmanagerwebsite.dto.UserRecoveryRequest;
import com.example.mailmanagerwebsite.dto.UserRegisterRequest;
import com.example.mailmanagerwebsite.enums.Status;
import com.example.mailmanagerwebsite.model.User;
import com.example.mailmanagerwebsite.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean isExisted(String vmail) {
        Optional<User> opt = repository.findByVmail(vmail);
        return opt.isPresent();
    }

    public boolean isExisted(int id) {
        Optional<User> opt = repository.findById(id);
        return opt.isPresent();
    }

    @Transactional
    public Optional<UserDTO> getUserForLogin(UserLoginRequest req) {
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
    public Optional<UserDTO> getUserForLogin(int id) {
        Optional<User> opt = repository.findById(id);
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
            user.setUsername(req.getUsername());
            user.setPhone(req.getPhone());
            user.setCreated(LocalDateTime.now());
            user.setStatus(Status.ACTIVE);
            this.repository.save(user);
            return Optional.of(UserDTO.convert(user));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean updateUserPassword(UserRecoveryRequest req) {
        req.setPassword(DigestUtils.sha256Hex(req.getVmail() + req.getPassword()));
        Optional<User> opt = repository.findByVmail(req.getVmail());
        if (opt.isPresent()) {
            User user = opt.get();
            user.setPassword(req.getPassword());
            this.repository.save(user);
            return true;
        }
        return false;
    }
}