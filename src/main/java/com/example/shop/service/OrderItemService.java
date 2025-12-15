package com.example.shop.service;

import com.example.shop.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderItemService {
    private final List<OrderItem> storage = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public OrderItem create(OrderItem item) {
        item.setId(idGenerator.getAndIncrement());
        storage.add(item);
        return item;
    }

    public List<OrderItem> findAll() {
        return storage;
    }

    public Optional<OrderItem> findById(Long id) {
        return storage.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public Optional<OrderItem> update(Long id, OrderItem newData) {
        Optional<OrderItem> existing = findById(id);
        if (existing.isPresent()) {
            OrderItem item = existing.get();
            item.setOrderId(newData.getOrderId());
            item.setProductId(newData.getProductId());
            item.setQuantity(newData.getQuantity());
            return Optional.of(item);
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        return storage.removeIf(i -> i.getId().equals(id));
    }
}