package com.example.shop.service;

import com.example.shop.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryService {
    private final List<Category> storage = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Category create(Category category) {
        category.setId(idGenerator.getAndIncrement());
        storage.add(category);
        return category;
    }

    public List<Category> findAll() {
        return storage;
    }

    public Optional<Category> findById(Long id) {
        return storage.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Category> update(Long id, Category newData) {
        Optional<Category> existing = findById(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            category.setName(newData.getName());
            return Optional.of(category);
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return storage.removeIf(c -> c.getId().equals(id));
    }
}