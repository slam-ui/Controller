package com.example.shop.service;

import com.example.shop.entity.OrderItem;
import com.example.shop.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository repository;

    // Создать
    public OrderItem create(OrderItem item) {
        return repository.save(item);
    }

    // Получить все
    public List<OrderItem> findAll() {
        return repository.findAll();
    }

    // Получить по ID
    public Optional<OrderItem> findById(Long id) {
        return repository.findById(id);
    }

    // Обновить
    public Optional<OrderItem> update(Long id, OrderItem newData) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setOrder(newData.getOrder());
                    existing.setProduct(newData.getProduct());
                    existing.setQuantity(newData.getQuantity());
                    return repository.save(existing);
                });
    }

    // Удалить
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}