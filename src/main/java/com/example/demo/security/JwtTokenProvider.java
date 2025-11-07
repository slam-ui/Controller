package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long jwtExpirationDate;

    public JwtTokenProvider(@Value("${app.jwt-secret}") String jwtSecret,
                            @Value("${app.jwt-expiration-milliseconds}") long jwtExpirationDate) {
        // Создаем ключ напрямую из строки секрета.
        // Этот способ более надежен, чем декодирование из Base64.
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationDate = jwtExpirationDate;
    }

    // Генерация JWT токена
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS512) // Указываем ключ и алгоритм
                .compact();
    }

    // Получение имени пользователя из токена
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Валидация токена
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            // Можно добавить логирование для отладки
            // e.printStackTrace();
            return false;
        }
    }
}