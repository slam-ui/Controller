package com.example.controller.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;      // ID пользователя
    private String refreshToken;  // Сам токен обновления
    private LocalDateTime expiresAt; // Когда токен протухнет

    @Enumerated(EnumType.STRING)
    private SessionStatus status; // Активна или нет

    // Конструктор
    public UserSession() {}

    public UserSession(Long customerId, String refreshToken, LocalDateTime expiresAt) {
        this.customerId = customerId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.status = SessionStatus.ACTIVE;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
}