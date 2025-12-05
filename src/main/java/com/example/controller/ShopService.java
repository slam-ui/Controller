package com.example.controller;

import com.example.controller.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private OrderRepository orderRepository;

    // Подключаем шифровальщик паролей
    @Autowired private PasswordEncoder passwordEncoder;

    // ОБНОВЛЕННАЯ РЕГИСТРАЦИЯ
    public Customer registerCustomer(String name, String email, String password, String role) {
        if (customerRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email уже занят!");
        }

        // 1. Проверка пароля (минимум 8 символов)
        if (password.length() < 8) {
            throw new RuntimeException("Пароль слишком короткий! Нужно минимум 8 символов.");
        }

        // 2. Шифруем пароль
        String encodedPassword = passwordEncoder.encode(password);

        // 3. Если роль не указана, по умолчанию USER
        String userRole = (role == null || role.isEmpty()) ? "USER" : role;

        Customer newCustomer = new Customer(name, email, encodedPassword, userRole);
        return customerRepository.save(newCustomer);
    }

    // --- Остальные методы (makeOrder, payOrder и т.д.) оставьте без изменений ---
    // (Ниже для примера метод покупки, убедитесь что он у вас есть)

    @Transactional
    public Order makeOrder(Long customerId, Long productId, Integer amount) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Клиент не найден"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getQuantity() < amount) {
            throw new RuntimeException("На складе недостаточно товара!");
        }

        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setAmount(amount);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(product.getPrice() * amount);
        order.setStatus("CREATED");

        return orderRepository.save(order);
    }

    public List<Order> getCustomerHistory(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getAmount());
        productRepository.save(product);
        order.setStatus("CANCELED");
        return orderRepository.save(order);
    }
}