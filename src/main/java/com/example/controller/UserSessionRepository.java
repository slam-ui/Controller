package com.example.controller; // Или ваш пакет репозиториев

import com.example.controller.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    // Найти активную сессию по токену
    Optional<UserSession> findByRefreshToken(String refreshToken);
}