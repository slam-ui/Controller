package com.example.shop.service;

import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    // Создать
    public User create(User user) {
        return repository.save(user);
    }

    // Получить все
    public List<User> findAll() {
        return repository.findAll();
    }

    // Получить по ID
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    // Обновить
    public Optional<User> update(Long id, User newData) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUsername(newData.getUsername());
                    existing.setEmail(newData.getEmail());
                    existing.setPassword(newData.getPassword());
                    return repository.save(existing);
                });
    }

    // Удалить
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}