package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private OrderRepository orderRepository;

    // БИЗНЕС-ОПЕРАЦИЯ 1: Регистрация клиента (с проверкой)
    public Customer registerCustomer(String name, String email) {
        if (customerRepository.findByEmail(email) != null) {
            throw new RuntimeException("Клиент с таким email уже существует!");
        }
        return customerRepository.save(new Customer(name, email));
    }

    // БИЗНЕС-ОПЕРАЦИЯ 2: Оформление заказа (Транзакция: создаем заказ + уменьшаем склад)
    @Transactional
    public Order makeOrder(Long customerId, Long productId, Integer amount) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Клиент не найден"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getQuantity() < amount) {
            throw new RuntimeException("На складе недостаточно товара!");
        }

        // Уменьшаем количество на складе
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);

        // Создаем заказ
        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setAmount(amount);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(product.getPrice() * amount);
        order.setStatus("CREATED");

        return orderRepository.save(order);
    }

    // БИЗНЕС-ОПЕРАЦИЯ 3: Оплата заказа (Меняем статус)
    public Order payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Заказ уже обработан или отменен");
        }
        order.setStatus("PAID");
        return orderRepository.save(order);
    }

    // БИЗНЕС-ОПЕРАЦИЯ 4: Отмена заказа (Транзакция: меняем статус + возвращаем товар на склад)
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        // Возвращаем товары на склад
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getAmount());
        productRepository.save(product);

        order.setStatus("CANCELED");
        return orderRepository.save(order);
    }

    // БИЗНЕС-ОПЕРАЦИЯ 5: Поиск всех заказов клиента
    public List<Order> getCustomerHistory(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}