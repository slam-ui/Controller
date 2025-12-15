package com.example.shop.service;

import com.example.shop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    // Наша "база данных" в памяти
    private final List<Product> storage = new ArrayList<>();
    // Генератор ID (1, 2, 3...)
    private final AtomicLong idGenerator = new AtomicLong(1);

    // 1. Создать
    public Product create(Product product) {
        product.setId(idGenerator.getAndIncrement());
        storage.add(product);
        return product;
    }

    // 2. Получить все
    public List<Product> findAll() {
        return storage;
    }

    // 3. Получить по ID
    public Optional<Product> findById(Long id) {
        return storage.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // 4. Обновить
    public Optional<Product> update(Long id, Product newData) {
        Optional<Product> existing = findById(id);
        if (existing.isPresent()) {
            Product product = existing.get();
            // Обновляем поля
            product.setName(newData.getName());
            product.setPrice(newData.getPrice());
            product.setDescription(newData.getDescription());
            product.setCategoryId(newData.getCategoryId());
            return Optional.of(product);
        }
        return Optional.empty();
    }

    // 5. Удалить
    public boolean delete(Long id) {
        return storage.removeIf(p -> p.getId().equals(id));
    }
}