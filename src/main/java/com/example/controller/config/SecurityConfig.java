package com.example.controller.config;

import com.example.controller.MyUserDetailsService; // Если этот класс в другом пакете, IDEA подскажет
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для REST API (так как мы используем Postman и Basic Auth)
                .csrf(AbstractHttpConfigurer::disable)
                // Настраиваем правила доступа
                .authorizeHttpRequests(auth -> auth
                        // Регистрация доступна всем (неавторизованным)
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        // Просмотр товаров доступен всем
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        // Добавлять товары может ТОЛЬКО админ
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                        // Все остальные запросы (покупка, история) требуют авторизации
                        .anyRequest().authenticated()
                )
                // Включаем Basic Auth (логин/пароль в заголовке запроса)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Шифрование паролей
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}