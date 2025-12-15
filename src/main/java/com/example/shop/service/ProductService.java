package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    // Create
    public Product create(Product product) {
        return repository.save(product);
    }

    // Read All
    public List<Product> findAll() {
        return repository.findAll();
    }

    // Read One
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    // Update
    public Optional<Product> update(Long id, Product newProduct) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(newProduct.getName());
                    existing.setPrice(newProduct.getPrice());
                    existing.setDescription(newProduct.getDescription());
                    existing.setCategory(newProduct.getCategory());
                    return repository.save(existing);
                });
    }

    // Delete
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}