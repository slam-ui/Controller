package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor; // <--- УБЕДИТЕСЬ, ЧТО ЭТОТ ИМПОРТ ЕСТЬ
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor // <--- ВОТ НЕДОСТАЮЩАЯ АННОТАЦИЯ
public class ProductController {

    private final ProductService productService;

    // Только для ADMIN
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    // Для всех
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    // Для всех
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Только для ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    // Только для ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- Эндпоинты для бизнес-операций ---

    // Для всех
    @GetMapping("/search/name")
    public List<ProductDto> searchByName(@RequestParam String keyword) {
        return productService.searchProductsByName(keyword);
    }

    // Для всех
    @GetMapping("/search/price")
    public List<ProductDto> searchByPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return productService.searchProductsByPriceRange(min, max);
    }
}