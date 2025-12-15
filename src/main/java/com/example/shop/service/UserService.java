package com.example.shop.service;

import com.example.shop.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final List<User> storage = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User create(User user) {
        user.setId(idGenerator.getAndIncrement());
        storage.add(user);
        return user;
    }

    public List<User> findAll() {
        return storage;
    }

    public Optional<User> findById(Long id) {
        return storage.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> update(Long id, User newData) {
        Optional<User> existing = findById(id);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setUsername(newData.getUsername());
            user.setEmail(newData.getEmail());
            user.setPassword(newData.getPassword());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return storage.removeIf(u -> u.getId().equals(id));
    }
}