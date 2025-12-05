package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    @Autowired private ShopService shopService;
    @Autowired private ProductRepository productRepository;

    // --- CRUD ДЛЯ ТОВАРОВ ---

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // --- БИЗНЕС-ОПЕРАЦИИ ---

    // 1. Регистрация
    @PostMapping("/register")
    public Customer register(@RequestParam String name, @RequestParam String email) {
        return shopService.registerCustomer(name, email);
    }

    // 2. Покупка (создание заказа)
    @PostMapping("/buy")
    public Order buy(@RequestParam Long customerId, @RequestParam Long productId, @RequestParam Integer amount) {
        return shopService.makeOrder(customerId, productId, amount);
    }

    // 3. История заказов клиента
    @GetMapping("/history/{customerId}")
    public List<Order> history(@PathVariable Long customerId) {
        return shopService.getCustomerHistory(customerId);
    }

    // 4. Оплата
    @PostMapping("/pay/{orderId}")
    public Order pay(@PathVariable Long orderId) {
        return shopService.payOrder(orderId);
    }

    // 5. Отмена
    @PostMapping("/cancel/{orderId}")
    public Order cancel(@PathVariable Long orderId) {
        return shopService.cancelOrder(orderId);
    }
}