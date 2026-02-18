package com.example.mailmanagerwebsite.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.embedded.DetailId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "detail")
public class Detail implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DetailId id;

    @Column(name = "seen", nullable = false, columnDefinition = "boolean")
    private boolean seen;

    @Column(name = "starred", nullable = false, columnDefinition = "boolean")
    private boolean starred;

    @Column(name = "trashed", nullable = false, columnDefinition = "boolean")
    private boolean trashed;

    @Column(name = "received", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime received;

    @ManyToOne
    @MapsId("email_id")
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("folder_id")
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    public Detail() {
        this.seen = false;
        this.starred = false;
        this.trashed = false;
        this.received = null;
    }

    public Detail(boolean seen, boolean starred, boolean trashed, LocalDateTime received) {
        this.seen = seen;
        this.starred = starred;
        this.trashed = trashed;
        this.received = received;
    }

    public Detail(DetailId id, boolean seen, boolean starred, boolean trashed, LocalDateTime received) {
        this.id = id;
        this.seen = seen;
        this.starred = starred;
        this.trashed = trashed;
        this.received = received;
    }

    public DetailId getId() {
        return id;
    }

    public void setId(DetailId id) {
        this.id = id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    public LocalDateTime getReceived() {
        return received;
    }

    public void setReceived(LocalDateTime received) {
        this.received = received;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return String.format("{ id: %s, seen: %b, starred: %b, trashed: %b, received: %s }", id, seen, starred, trashed, received.toString());
    }

}