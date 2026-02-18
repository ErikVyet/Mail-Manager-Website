package com.example.mailmanagerwebsite.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "folder")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "integer")
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)", unique = true)
    private String name;

    @Column(name = "system", nullable = false, columnDefinition = "boolean")
    private boolean system;

    @Column(name = "created", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "folder")
    private List<Detail> details;

    public Folder() {
        this.name = null;
        this.system = false;
        this.created = null;
    }

    public Folder(String name, boolean system, LocalDateTime created) {
        this.name = name;
        this.system = system;
        this.created = created;
    }

    public Folder(int id, String name, boolean system, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.system = system;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, name: %s, system: %b, created: %s }", id, name, system, created.toString());
    }

}