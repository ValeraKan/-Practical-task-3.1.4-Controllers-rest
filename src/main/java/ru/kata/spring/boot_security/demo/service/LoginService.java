package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.LoginRequest;
import ru.kata.spring.boot_security.demo.dto.LoginResponse;

import jakarta.servlet.http.HttpSession;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest, HttpSession session);

    LoginResponse logout(HttpSession session);
}