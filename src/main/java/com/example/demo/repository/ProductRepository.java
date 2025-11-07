package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Верните этот метод для поиска по имени
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Верните этот метод для поиска по диапазону цен
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}