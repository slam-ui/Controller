// Файл: src/main/java/com/example/demo/service/AuthService.java
package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}