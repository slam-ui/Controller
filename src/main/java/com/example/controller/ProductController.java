package com.example.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/products") // Базовый адрес для всех методов: localhost:8080/products
public class ProductController {

    // Имитация базы данных в памяти
    private List<Product> products = new ArrayList<>();
    // Генератор уникальных ID
    private AtomicLong idCounter = new AtomicLong();

    // 1. ПОЛУЧЕНИЕ ВСЕХ ТОВАРОВ (GET)
    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    // 2. ПОЛУЧЕНИЕ ОДНОГО ТОВАРА ПО ID (GET)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null); // Если не найдено, вернет null (пустоту)
    }

    // 3. СОЗДАНИЕ ТОВАРА (POST)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Генерируем новый ID и присваиваем товару
        product.setId(idCounter.incrementAndGet());
        products.add(product);
        return product;
    }

    // 4. ИЗМЕНЕНИЕ ТОВАРА (PUT)
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product != null) {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
        }
        return product;
    }

    // 5. УДАЛЕНИЕ ТОВАРА (DELETE)
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed) {
            return "Товар с ID " + id + " был удален.";
        } else {
            return "Товар с таким ID не найден.";
        }
    }
}