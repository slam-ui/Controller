package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    // Создать
    public Order create(Order order) {
        // Здесь мы предполагаем, что в order уже передан объект User
        return repository.save(order);
    }

    // Получить все
    public List<Order> findAll() {
        return repository.findAll();
    }

    // Получить по ID
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    // Обновить
    public Optional<Order> update(Long id, Order newData) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUser(newData.getUser());
                    existing.setStatus(newData.getStatus());
                    existing.setCreatedDate(newData.getCreatedDate());
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