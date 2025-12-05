package com.example.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/math") // Все пути в этом контроллере будут начинаться с /math
public class MathController {

    // Пример сложения (пример: /math/add?a=10&b=20)
    @GetMapping("/add")
    public String add(@RequestParam int a, @RequestParam int b) {
        int sum = a + b;
        return String.format("%d + %d = %d", a, b, sum);
    }

    // Пример получения числа из самого пути (пример: /math/square/5)
    @GetMapping("/square/{number}")
    public String square(@PathVariable int number) {
        return "Квадрат числа " + number + " равен " + (number * number);
    }
}