package com.example.controller;

import com.example.controller.model.Customer;
import com.example.controller.model.Order;
import com.example.controller.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Репозиторий товаров
@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
}

// Репозиторий клиентов
@Repository
interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}

// Репозиторий заказов
@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
}