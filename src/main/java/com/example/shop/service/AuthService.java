package com.example.shop.service;

import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password, String email) {
        // 1. Проверка существования
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        // 2. Валидация пароля (Задание: длина и спецсимволы)
        if (password.length() < 8) {
            throw new RuntimeException("Пароль должен быть не менее 8 символов");
        }
        // Простая регулярка: содержит хотя бы одну цифру, букву и спецсимвол
        // Для простоты пока проверим только спецсимвол или оставим только длину.
        // Пример проверки на спецсимвол:
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new RuntimeException("Пароль должен содержать спецсимвол");
        }

        // 3. Создание пользователя
        User user = new User();
        user.setUsername(username);
        // ВАЖНО: Пароль в базу пишем ТОЛЬКО зашифрованным
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.USER); // По умолчанию регистрируем как USER

        return userRepository.save(user);
    }
}