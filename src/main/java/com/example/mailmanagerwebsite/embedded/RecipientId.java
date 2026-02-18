package com.example.mailmanagerwebsite.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecipientId implements Serializable {

    @Column(name = "email_id", nullable = false)
    private int email_id;

    @Column(name = "user_id", nullable = false)
    private int user_id;

    public RecipientId() { }

    public RecipientId(int email_id, int user_id) {
        this.email_id = email_id;
        this.user_id = user_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email_id, user_id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecipientId other = (RecipientId) obj;
        if (email_id != other.email_id)
            return false;
        if (user_id != other.user_id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("{ email_id: %d, user_id: %d }", email_id, user_id);
    }

}