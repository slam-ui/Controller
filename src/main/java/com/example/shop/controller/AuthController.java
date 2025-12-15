package com.example.shop.controller;

import com.example.shop.entity.User;
import com.example.shop.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request) {
        return authService.register(request.getUsername(), request.getPassword(), request.getEmail());
    }

    // DTO класс для приема данных
    @Data
    public static class RegistrationRequest {
        private String username;
        private String password;
        private String email;
    }
}