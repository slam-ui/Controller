package com.example.controller;

public class Product {
    private Long id;
    private String name;
    private Double price;

    // Конструктор по умолчанию
    public Product() {
    }

    // Конструктор с параметрами
    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Геттеры и сеттеры (чтобы Spring мог читать и менять поля)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}