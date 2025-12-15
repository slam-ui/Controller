package com.example.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        // Здесь можно указать свой номер группы и студенческий, как просит преподаватель
        return "Hello! Проект Интернет-магазин работает. Студент: Андреев Н.А.";
    }
}