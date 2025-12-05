package com.example.controller.config;

import com.example.controller.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired private JwtCore jwtCore;
    @Autowired private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String email = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;

        try {
            // Читаем заголовок Authorization: Bearer <token>
            String headerAuth = request.getHeader("Authorization");

            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7); // Убираем слово "Bearer "
            }

            // Если токен есть, валидируем его
            if (jwt != null) {
                try {
                    email = jwtCore.getEmailFromToken(jwt);
                } catch (ExpiredJwtException e) {
                    // Если время жизни вышло
                }

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = userDetailsService.loadUserByUsername(email);
                    auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            // Ошибки игнорируем, просто не авторизуем
        }

        filterChain.doFilter(request, response);
    }
}