package com.example.shop.controller;

import com.example.shop.entity.Order;
import com.example.shop.entity.Product;
import com.example.shop.service.ShopBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final ShopBusinessService businessService;

    // POST /api/business/order?userId=1
    // Body: {"1": 2, "5": 1} (ID товара : Количество)
    @PostMapping("/order")
    public Order placeOrder(@RequestParam Long userId, @RequestBody Map<Long, Integer> items) {
        return businessService.createOrder(userId, items);
    }

    // GET /api/business/search?category=Electronics&min=100&max=2000
    @GetMapping("/search")
    public List<Product> search(@RequestParam String category,
                                @RequestParam BigDecimal min,
                                @RequestParam BigDecimal max) {
        return businessService.searchProducts(category, min, max);
    }
}