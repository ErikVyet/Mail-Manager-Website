package com.example.mailmanagerwebsite.model;

import java.io.Serializable;

import com.example.mailmanagerwebsite.embedded.RecipientId;
import com.example.mailmanagerwebsite.enums.Type;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipient")
public class Recipient implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RecipientId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20)")
    private Type type;

    @ManyToOne
    @MapsId("email_id")
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Recipient() {
        this.type = null;
    }

    public Recipient(Type type) {
        this.type = type;
    }

    public Recipient(RecipientId id, Type type) {
        this.id = id;
        this.type = type;
    }

    public RecipientId getId() {
        return id;
    }

    public void setId(RecipientId id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("{ id: %s, type: %s }", id, type);
    }

}