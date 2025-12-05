package com.example.controller;

import com.example.controller.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    @Autowired private ShopService shopService;
    @Autowired private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // ОБНОВЛЕННЫЙ МЕТОД РЕГИСТРАЦИИ
    @PostMapping("/register")
    public Customer register(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(defaultValue = "USER") String role) {
        return shopService.registerCustomer(name, email, password, role);
    }

    @PostMapping("/buy")
    public Order buy(@RequestParam Long customerId, @RequestParam Long productId, @RequestParam Integer amount) {
        return shopService.makeOrder(customerId, productId, amount);
    }

    @GetMapping("/history/{customerId}")
    public List<Order> history(@PathVariable Long customerId) {
        return shopService.getCustomerHistory(customerId);
    }

    @PostMapping("/cancel/{orderId}")
    public Order cancel(@PathVariable Long orderId) {
        return shopService.cancelOrder(orderId);
    }
}