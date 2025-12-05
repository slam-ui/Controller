package com.example.controller;

import com.example.controller.config.JwtCore;
import com.example.controller.model.Customer;
import com.example.controller.model.SessionStatus;
import com.example.controller.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private UserSessionRepository userSessionRepository;
    @Autowired private JwtCore jwtCore;
    @Autowired private PasswordEncoder passwordEncoder;

    // ЛОГИН
    public Map<String, String> login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null || !passwordEncoder.matches(password, customer.getPassword())) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        // Генерируем пару токенов
        String accessToken = jwtCore.generateAccessToken(customer.getEmail(), customer.getRole());
        String refreshToken = jwtCore.generateRefreshToken(customer.getEmail());

        // Сохраняем сессию в БД
        UserSession session = new UserSession(customer.getId(), refreshToken, LocalDateTime.now().plusDays(30));
        userSessionRepository.save(session);

        // Возвращаем токены клиенту
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    // ОБНОВЛЕНИЕ ТОКЕНОВ (REFRESH)
    public Map<String, String> refresh(String refreshToken) {
        // 1. Валидируем токен (технически)
        if (!jwtCore.validateToken(refreshToken)) {
            throw new RuntimeException("Невалидный Refresh токен");
        }

        // 2. Ищем сессию в БД
        UserSession session = userSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Сессия не найдена"));

        // 3. Проверяем, не истекла ли сессия и активна ли она
        if (session.getExpiresAt().isBefore(LocalDateTime.now()) || session.getStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("Сессия истекла или закрыта");
        }

        // 4. Получаем данные пользователя
        Customer customer = customerRepository.findById(session.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // 5. Генерируем НОВУЮ пару
        String newAccessToken = jwtCore.generateAccessToken(customer.getEmail(), customer.getRole());
        String newRefreshToken = jwtCore.generateRefreshToken(customer.getEmail());

        // 6. Обновляем сессию (старый refresh больше не работает)
        session.setRefreshToken(newRefreshToken);
        session.setExpiresAt(LocalDateTime.now().plusDays(30));
        userSessionRepository.save(session);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return tokens;
    }
}