package com.example.shop.service;

import com.example.shop.dto.LoginRequest;
import com.example.shop.dto.TokenResponse;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.entity.UserSession;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.UserSessionRepository;
import com.example.shop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь существует");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        // Генерируем пару токенов
        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        // Сохраняем сессию в БД
        UserSession session = new UserSession();
        session.setUser(user);
        session.setRefreshToken(refreshToken);
        session.setExpiresAt(LocalDateTime.now().plusDays(30));
        userSessionRepository.save(session);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        // 1. Ищем сессию в БД
        UserSession session = userSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Невалидный Refresh токен"));

        // 2. Проверяем не протухла ли
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            userSessionRepository.delete(session);
            throw new RuntimeException("Refresh токен истек");
        }

        User user = session.getUser();

        // 3. Удаляем старую сессию (Rotation)
        userSessionRepository.delete(session);

        // 4. Создаем новую пару и сохраняем
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        UserSession newSession = new UserSession();
        newSession.setUser(user);
        newSession.setRefreshToken(newRefreshToken);
        newSession.setExpiresAt(LocalDateTime.now().plusDays(30));
        userSessionRepository.save(newSession);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}