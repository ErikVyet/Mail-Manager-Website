package com.example.mailmanagerwebsite.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "token")
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, columnDefinition = "integer")
    private int id;

    @Column(name = "value", nullable = false, unique = true, columnDefinition = "char(64)")
    private String value;

    @Column(name = "created", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime created;

    @Column(name = "expiry", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime expiry;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Token() {
        this.value = null;
        this.created = null;
        this.expiry = null;
    }

    public Token(String value, LocalDateTime created, LocalDateTime expiry) {
        this.value = value;
        this.created = created;
        this.expiry = expiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{ id:" + id + ", value:" + value + ", created:" + created + ", expiry:" + expiry + " }";
    }
    
}