package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;

import com.example.mailmanagerwebsite.validation.NotBlankValidation;
import com.example.mailmanagerwebsite.validation.PatternValidation;
import com.example.mailmanagerwebsite.validation.SizeValidation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Vmail is required", groups = NotBlankValidation.class)
    @Size(min = 11, max = 255, message = "Vmail must be between 11 and 255 characters", groups = SizeValidation.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+@vmail\\.com$", message = "Vmail must be in the correct format", groups = PatternValidation.class)
    private String vmail;

    @NotBlank(message = "Password is required", groups = NotBlankValidation.class)
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters", groups = SizeValidation.class)
    private String password;

    public UserLoginRequest() { }

    public UserLoginRequest(String vmail, String password) {
        this.vmail = vmail;
        this.password = password;
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

}