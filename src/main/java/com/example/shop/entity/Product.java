package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price; // Для денег лучше использовать BigDecimal
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id") // В таблице будет колонка category_id
    private Category category;
}