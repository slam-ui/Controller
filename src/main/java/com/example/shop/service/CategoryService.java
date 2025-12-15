package com.example.shop.service;

import com.example.shop.entity.Category;
import com.example.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    // Создать
    public Category create(Category category) {
        return repository.save(category);
    }

    // Получить все
    public List<Category> findAll() {
        return repository.findAll();
    }

    // Получить по ID
    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    // Обновить
    public Optional<Category> update(Long id, Category newData) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(newData.getName());
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