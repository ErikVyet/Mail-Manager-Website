package com.example.mailmanagerwebsite.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.EmailDTO;
import com.example.mailmanagerwebsite.dto.UserDTO;
import com.example.mailmanagerwebsite.model.Email;
import com.example.mailmanagerwebsite.repository.EmailRepository;

@Service
public class EmailService {

    protected final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Transactional(readOnly = true)
    public Optional<EmailDTO> getEmail(int id) {
        Optional<Email> optEmail = this.emailRepository.findById(id);
        if (!optEmail.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(EmailDTO.convert(optEmail.get()));
    }

    public Optional<EmailDTO> createEmail(EmailDTO emailDTO) {
        try {
            Email email = new Email(
                emailDTO.getSubject(),
                emailDTO.getBody(),
                emailDTO.getSent()
            );
            email.setSender(UserDTO.revert(emailDTO.getSender()));
            return Optional.of(EmailDTO.convert(this.emailRepository.save(email)));
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteEmail(int id) {
        try {
            this.emailRepository.deleteById(id);
            return true;
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public boolean updateEmail(EmailDTO emailDTO) {
        try {
            Optional<Email> optEmail = this.emailRepository.findById(emailDTO.getId());
            if (!optEmail.isPresent()) {
                return false;
            }
            Email email = optEmail.get();
            email.setSubject(emailDTO.getSubject());
            email.setBody(emailDTO.getBody());
            email.setSent(emailDTO.getSent());
            email.setSender(UserDTO.revert(emailDTO.getSender()));
            this.emailRepository.save(email);
            return true;
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

}