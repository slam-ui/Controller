package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";

    // Конструктор, который принимает только токен доступа
    public JwtAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}