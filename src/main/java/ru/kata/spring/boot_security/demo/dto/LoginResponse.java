package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class LoginResponse {
    private String status;
    private String message;
    private List<String> roles;

    public LoginResponse(String status, String message, List<String> roles) {
        this.status = status;
        this.message = message;
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}