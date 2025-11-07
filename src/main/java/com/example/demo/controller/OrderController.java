package com.example.demo.controller;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Для USER и ADMIN
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        String username = getCurrentUsername();
        return new ResponseEntity<>(orderService.createOrder(username, orderRequestDto), HttpStatus.CREATED);
    }

    // Для USER и ADMIN - получить свои заказы
    @GetMapping
    public List<OrderResponseDto> getMyOrders() {
        String username = getCurrentUsername();
        return orderService.getOrdersForUser(username);
    }

    // Только для ADMIN
    @GetMapping("/all")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}