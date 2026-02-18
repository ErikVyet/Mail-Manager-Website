package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.enums.Status;
import com.example.mailmanagerwebsite.model.User;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String vmail;
    private String phone;
    private String username;
    private String fullname;
    private byte[] avatar;
    private LocalDateTime created;
    private LocalDateTime login;
    private Status status;

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

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setVmail(user.getVmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullname(user.getFullname());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setCreated(user.getCreated());
        userDTO.setLogin(user.getLogin());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }

    public static User revert(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setVmail(userDTO.getVmail());
        user.setPhone(userDTO.getPhone());
        user.setUsername(userDTO.getUsername());
        user.setFullname(userDTO.getFullname());
        user.setAvatar(userDTO.getAvatar());
        user.setCreated(userDTO.getCreated());
        user.setLogin(userDTO.getLogin());
        user.setStatus(userDTO.getStatus());
        return user;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, vmail: %s, phone: %s, username: %s, fullname: %s, avatar: %s, created: %s, login: %s, status: %s }", id, vmail, phone, username, fullname, avatar, created.toString(), login.toString(), status);
    }

}