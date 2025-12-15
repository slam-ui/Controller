package com.example.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop") // Общий префикс для всех методов этого контроллера
public class ShopController {

    // Получить статус магазина
    // URL: http://localhost:8080/api/shop/status
    @GetMapping("/status")
    public String getStatus() {
        return "Магазин работает в штатном режиме.";
    }

    // Получить информацию о магазине
    // URL: http://localhost:8080/api/shop/info
    @GetMapping("/info")
    public String getShopInfo() {
        return "Добро пожаловать в 'TechStore'! Мы продаем лучшую электронику.";
    }

    // Пример метода с параметром (поиск)
    // URL: http://localhost:8080/api/shop/search?query=iphone
    @GetMapping("/search")
    public String searchProduct(@RequestParam(name = "query", defaultValue = "") String query) {
        if (query.isEmpty()) {
            return "Вы ничего не искали.";
        }
        return "Вы ищете: " + query + ". (Поиск пока не реализован, ждите Задание 2)";
    }
}