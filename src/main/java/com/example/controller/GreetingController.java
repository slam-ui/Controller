package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    // Простой эндпоинт по корневому пути
    @GetMapping("/")
    public String index() {
        return "Приложение работает! Используйте /hello или /math";
    }

    // Эндпоинт с параметром (пример: /hello?name=Ivan)
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "Student") String name) {
        return "Привет, " + name + "! Задание выполнено.";
    }
}