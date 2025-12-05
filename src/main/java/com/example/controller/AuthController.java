package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthService authService;

    // Вход по Email/Password -> Возвращает Access и Refresh токены
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }

    // Обновление токенов по Refresh токену
    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }
}