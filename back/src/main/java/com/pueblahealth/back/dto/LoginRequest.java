package com.pueblahealth.back.dto;

public class LoginRequest {

    private String email;
    private String emailIv;

    private String password;
    private String passwordIv;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailIv() {
        return emailIv;
    }

    public void setEmailIv(String emailIv) {
        this.emailIv = emailIv;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordIv() {
        return passwordIv;
    }

    public void setPasswordIv(String passwordIv) {
        this.passwordIv = passwordIv;
    }
}