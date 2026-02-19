package com.example.mailmanagerwebsite.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.example.mailmanagerwebsite.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "integer")
    private int id;

    @Column(name = "vmail", nullable = false, columnDefinition = "varchar(255)", unique = true)
    private String vmail;

    @Column(name = "phone", nullable = false, columnDefinition = "char(10)", unique = true)
    private String phone;

    @Column(name = "username", nullable = false, columnDefinition = "varchar(50)", unique = true)
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(64)")
    private String password;

    @Column(name = "fullname", nullable = false, columnDefinition = "varchar(100)")
    private String fullname;

    @Column(name = "avatar", nullable = true, columnDefinition = "bytea")
    private byte[] avatar;

    @Column(name = "created", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime created;

    @Column(name = "login", nullable = true, columnDefinition = "timestamp")
    private LocalDateTime login;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20)")
    private Status status;

    @OneToMany(mappedBy = "user")
    private List<Folder> folders;

    @OneToMany(mappedBy = "sender")
    private List<Email> emails;

    @OneToMany(mappedBy = "user")
    private List<Recipient> recipients;

    @OneToMany(mappedBy = "user")
    private List<Detail> details;

    public User() {
        this.vmail = null;
        this.phone = null;
        this.username = null;
        this.password = null;
        this.fullname = null;
        this.avatar = null;
        this.created = null;
        this.login = null;
        this.status = null;
    }

    public User(String vmail, String phone, String username, String password, String fullname, byte[] avatar, LocalDateTime created, LocalDateTime login, Status status) {
        this.vmail = vmail;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.avatar = avatar;
        this.created = created;
        this.login = login;
        this.status = status;
    }

    public User(int id, String vmail, String phone, String username, String password, String fullname, byte[] avatar, LocalDateTime created, LocalDateTime login, Status status) {
        this.id = id;
        this.vmail = vmail;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.avatar = avatar;
        this.created = created;
        this.login = login;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVmail() {
        return vmail;
    }

    public void setVmail(String vmail) {
        this.vmail = vmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLogin() {
        return login;
    }

    public void setLogin(LocalDateTime login) {
        this.login = login;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, vmail: %s, phone: %s, username: %s, password: %s, fullname: %s, avatar: %s, created: %s, login: %s, status: %s }", id, vmail, phone, username, password, fullname, avatar, created.toString(), login.toString(), status);
    }

}