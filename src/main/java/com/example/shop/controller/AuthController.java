package com.example.shop.controller;

import com.example.shop.dto.LoginRequest;
import com.example.shop.dto.RefreshRequest;
import com.example.shop.dto.TokenResponse;
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
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request.getUsername(), request.getPassword(), request.getEmail());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refreshToken(request.getRefreshToken());
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;
    }
}