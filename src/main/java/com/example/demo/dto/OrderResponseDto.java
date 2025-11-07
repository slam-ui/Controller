package com.example.demo.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data public class OrderResponseDto { private Long id; private String username; private List<OrderItemResponseDto> items; private LocalDateTime orderDate; private BigDecimal totalPrice; private String status; }