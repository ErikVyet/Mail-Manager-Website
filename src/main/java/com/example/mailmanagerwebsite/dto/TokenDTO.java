package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String token;
    private LocalDateTime created;
    private LocalDateTime expiry;
    private int userId;
    
    public TokenDTO() {
        this.token = null;
        this.created = null;
        this.expiry = null;
        this.userId = -1;
    }

    public TokenDTO(String token, LocalDateTime created, LocalDateTime expiry, int userId) {
        this.token = token;
        this.created = created;
        this.expiry = expiry;
        this.userId = userId;
    }

    public TokenDTO(int id, String token, LocalDateTime created, LocalDateTime expiry, int userId) {
        this.id = id;
        this.token = token;
        this.created = created;
        this.expiry = expiry;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{ id:" + id + ", token:" + token + ", created:" + created + ", expiry:" + expiry + "}";
    }

}