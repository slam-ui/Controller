package com.example.shop.service;

import com.example.shop.entity.*;
import com.example.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShopBusinessService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 1. ОФОРМЛЕНИЕ ЗАКАЗА (Транзакция: Создать заказ -> Сохранить позиции)
    @Transactional
    public Order createOrder(Long userId, Map<Long, Integer> productIdsAndQuantities) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus("NEW");

        // Сначала сохраняем заказ, чтобы получить ID
        Order savedOrder = orderRepository.save(order);

        // Создаем позиции
        List<OrderItem> items = productIdsAndQuantities.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Товар не найден"));
                    OrderItem item = new OrderItem();
                    item.setOrder(savedOrder);
                    item.setProduct(product);
                    item.setQuantity(entry.getValue());
                    return item;
                }).toList();

        savedOrder.setItems(items);
        return orderRepository.save(savedOrder);
    }

    // 2. УМНЫЙ ПОИСК (Товары по категории и цене)
    public List<Product> searchProducts(String categoryName, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByCategoryNameAndPriceBetween(categoryName, minPrice, maxPrice);
    }

    // 3. ИСТОРИЯ ЗАКАЗОВ С ДЕТАЛЯМИ
    // В реальности лучше использовать DTO, но пока вернем Entity
    public List<Order> getUserHistory(Long userId) {
        // Здесь можно было бы использовать кастомный запрос в репозитории
        // findAll().stream()... (не эффективно, но для учебы пойдет)
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .toList();
    }

    // 4. МАССОВОЕ ИЗМЕНЕНИЕ ЦЕН (например, скидка на категорию)
    @Transactional
    public void applyDiscountToCategory(Long categoryId, int discountPercent) {
        List<Product> products = productRepository.findAll().stream()
                .filter(p -> p.getCategory().getId().equals(categoryId))
                .toList();

        for (Product p : products) {
            BigDecimal oldPrice = p.getPrice();
            BigDecimal discount = oldPrice.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100));
            p.setPrice(oldPrice.subtract(discount));
            productRepository.save(p);
        }
    }

    // 5. ПОЛУЧЕНИЕ ИНФОРМАЦИИ О СТАТУСЕ ЗАКАЗА
    public String checkOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        return "Заказ #" + orderId + " находится в статусе: " + order.getStatus();
    }
}