package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto orderRequestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь " + username + " не найден"));

        Order order = new Order();
        order.setCustomer(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequestDto itemDto : orderRequestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + itemDto.getProductId() + " не найден"));

            if (product.getQuantity() < itemDto.getQuantity()) {
                throw new InsufficientStockException("Недостаточно товара '" + product.getName() + "' на складе.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItems.add(orderItem);

            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            product.setQuantity(product.getQuantity() - itemDto.getQuantity());
            productRepository.save(product);
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return toResponseDto(savedOrder);
    }

    public List<OrderResponseDto> getOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь " + username + " не найден"));

        return orderRepository.findByCustomer_Id(user.getId()).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUsername(order.getCustomer().getUsername());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setItems(order.getItems().stream().map(this::toOrderItemResponseDto).collect(Collectors.toList()));
        return dto;
    }

    private OrderItemResponseDto toOrderItemResponseDto(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPriceAtPurchase());
        return dto;
    }
}