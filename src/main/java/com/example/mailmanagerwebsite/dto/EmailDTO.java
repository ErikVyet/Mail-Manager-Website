package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.model.Email;

public class EmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String subject;
    private String body;
    private LocalDateTime sent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public static EmailDTO convert(Email email) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId(email.getId());
        emailDTO.setSubject(email.getSubject());
        emailDTO.setBody(email.getBody());
        emailDTO.setSent(email.getSent());
        return emailDTO;
    }

    public static Email revert(EmailDTO emailDTO) {
        Email email = new Email();
        email.setId(emailDTO.getId());
        email.setSubject(emailDTO.getSubject());
        email.setBody(emailDTO.getBody());
        email.setSent(emailDTO.getSent());
        return email;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, subject: %s, body: %s, sent: %s }", id, subject, body, sent.toString());
    }

}