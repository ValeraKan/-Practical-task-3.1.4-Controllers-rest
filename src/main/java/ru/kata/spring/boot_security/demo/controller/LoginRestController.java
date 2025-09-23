package ru.kata.spring.boot_security.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.LoginRequest;
import ru.kata.spring.boot_security.demo.dto.LoginResponse;
import ru.kata.spring.boot_security.demo.service.LoginService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

    private final LoginService loginService;

    public LoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpSession session) {
        LoginResponse response = loginService.login(loginRequest, session);

        if ("error".equals(response.getStatus())) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        return ResponseEntity.ok(loginService.logout(session));
    }
}