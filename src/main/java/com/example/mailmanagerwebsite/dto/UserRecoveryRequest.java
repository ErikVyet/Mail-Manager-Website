package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;

import com.example.mailmanagerwebsite.validation.NotBlankValidation;
import com.example.mailmanagerwebsite.validation.PatternValidation;
import com.example.mailmanagerwebsite.validation.SizeValidation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRecoveryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Vmail is required", groups = NotBlankValidation.class)
    @Size(min = 11, max = 255, message = "Vmail must be between 11 and 255 characters", groups = SizeValidation.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+@vmail\\.com$", message = "Vmail must be in the correct format", groups = PatternValidation.class)
    private String vmail;

    @NotBlank(message = "Code is required", groups = NotBlankValidation.class)
    @Size(min = 6, max = 6, message = "Code must be 6 characters", groups = SizeValidation.class)
    private String code;

    @NotBlank(message = "Password is required", groups = NotBlankValidation.class)
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters", groups = SizeValidation.class)
    private String password;

     @NotBlank(message = "Confirm password is required", groups = NotBlankValidation.class)
    @Size(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters", groups = SizeValidation.class)
    private String confirmPassword;

    public UserRecoveryRequest() { }

    public UserRecoveryRequest(String vmail, String code, String password, String confirmPassword) {
        this.vmail = vmail;
        this.code = code;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getVmail() {
        return vmail;
    }

    public void setVmail(String vmail) {
        this.vmail = vmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}