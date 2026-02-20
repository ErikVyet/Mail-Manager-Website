package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;

import com.example.mailmanagerwebsite.validation.NotBlankValidation;
import com.example.mailmanagerwebsite.validation.PatternValidation;
import com.example.mailmanagerwebsite.validation.SizeValidation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Fullname is required", groups = NotBlankValidation.class)
    @Size(min = 1, max = 100, message = "Fullname must be between 1 and 100 characters", groups = SizeValidation.class)
    private String fullname;

    @NotBlank(message = "Username is required", groups = NotBlankValidation.class)
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters", groups = SizeValidation.class)
    private String username;

    @NotBlank(message = "Vmail is required", groups = NotBlankValidation.class)
    @Size(min = 1, max = 255, message = "Vmail must be between 1 and 255 characters", groups = SizeValidation.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+@vmail\\.com$", message = "Vmail must be in the correct format", groups = PatternValidation.class)
    private String vmail;

    @NotBlank(message = "Phone number is required", groups = NotBlankValidation.class)
    @Size(min = 10, max = 10, message = "Phone number must be 10 digits", groups = SizeValidation.class)
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits", groups = PatternValidation.class)
    private String phone;

    @NotBlank(message = "Code is required", groups = NotBlankValidation.class)
    @Size(min = 6, max = 6, message = "Code must be 6 characters", groups = SizeValidation.class)
    private String code;

    @NotBlank(message = "Password is required", groups = NotBlankValidation.class)
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters", groups = SizeValidation.class)
    private String password;

    @NotBlank(message = "Confirm password is required", groups = NotBlankValidation.class)
    @Size(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters", groups = SizeValidation.class)
    private String confirmPassword;

    public UserRegisterRequest() { }

    public UserRegisterRequest(String fullname, String username, String vmail, String phone, String password, String confirmPassword) {
        this.fullname = fullname;
        this.username = username;
        this.vmail = vmail;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVmail() {
        return vmail;
    }

    public void setVmail(String vmail) {
        this.vmail = vmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}