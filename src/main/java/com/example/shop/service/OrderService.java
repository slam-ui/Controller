package com.example.shop.service;

import com.example.shop.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {
    private final List<Order> storage = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Order create(Order order) {
        order.setId(idGenerator.getAndIncrement());
        // В реальном приложении здесь ставилась бы текущая дата
        storage.add(order);
        return order;
    }

    public List<Order> findAll() {
        return storage;
    }

    public Optional<Order> findById(Long id) {
        return storage.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public Optional<Order> update(Long id, Order newData) {
        Optional<Order> existing = findById(id);
        if (existing.isPresent()) {
            Order order = existing.get();
            order.setUserId(newData.getUserId());
            order.setStatus(newData.getStatus());
            order.setCreatedAt(newData.getCreatedAt());
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return storage.removeIf(o -> o.getId().equals(id));
    }
}