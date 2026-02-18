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
@Table(name = "attachment")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "integer")
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "data", nullable = false, columnDefinition = "bytea")
    private byte[] data;

    @Column(name = "size", nullable = false, columnDefinition = "integer")
    private int size;

    @Column(name = "mime", nullable = false, columnDefinition = "varchar(255)")
    private String mime;

    @Column(name = "uploaded", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime uploaded;

    @ManyToOne
    @JoinColumn(name = "email_id", nullable = false)
    private Email email;

    public Attachment() {
        this.name = null;
        this.data = null;
        this.size = 0;
        this.mime = null;
        this.uploaded = null;
    }

    public Attachment(String name, byte[] data, int size, String mime, LocalDateTime uploaded) {
        this.name = name;
        this.data = data;
        this.size = size;
        this.mime = mime;
        this.uploaded = uploaded;
    }

    public Attachment(int id, String name, byte[] data, int size, String mime, LocalDateTime uploaded) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.size = size;
        this.mime = mime;
        this.uploaded = uploaded;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, name: %s, data: %s, size: %d, mime: %s, uploaded: %s }", id, name, data, size, mime, uploaded.toString());
    }

}