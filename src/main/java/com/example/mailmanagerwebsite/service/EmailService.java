package com.example.mailmanagerwebsite.service;

import org.springframework.stereotype.Service;

import com.example.mailmanagerwebsite.repository.EmailRepository;

@Service
public class EmailService {

    protected final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

}