package com.example.controller.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtCore {

    // Секретный ключ (в реальном проекте хранить в env переменной!)
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // Время жизни Access токена (15 минут)
    private final long ACCESS_TOKEN_LIFETIME = 1000 * 60 * 15;
    // Время жизни Refresh токена (30 дней)
    private final long REFRESH_TOKEN_LIFETIME = 1000L * 60 * 60 * 24 * 30;

    // Генерация Access токена
    public String generateAccessToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // Добавляем роль внутрь токена
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Генерация Refresh токена
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_LIFETIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Проверка токена
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Получение Email из токена
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}