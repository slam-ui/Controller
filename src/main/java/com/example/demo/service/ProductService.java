package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // --- CRUD операции ---

    /**
     * Создает новый товар.
     * @param productDto DTO с данными нового товара.
     * @return DTO созданного товара с присвоенным ID.
     */
    public ProductDto createProduct(ProductDto productDto) {
        Product product = toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    /**
     * Возвращает список всех товаров.
     * @return Список DTO всех товаров.
     */
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает товар по его ID.
     * @param id ID товара.
     * @return DTO найденного товара.
     * @throws ResourceNotFoundException если товар с таким ID не найден.
     */
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + id + " не найден"));
        return toDto(product);
    }

    /**
     * Обновляет существующий товар.
     * @param id ID товара для обновления.
     * @param productDto DTO с новыми данными.
     * @return DTO обновленного товара.
     * @throws ResourceNotFoundException если товар с таким ID не найден.
     */
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + id + " не найден"));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        return toDto(updatedProduct);
    }

    /**
     * Удаляет товар по его ID.
     * @param id ID товара для удаления.
     * @throws ResourceNotFoundException если товар с таким ID не найден.
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Товар с ID " + id + " не найден");
        }
        productRepository.deleteById(id);
    }

    // --- Бизнес-операции ---

    /**
     * Ищет товары по ключевому слову в названии (без учета регистра).
     * @param keyword Ключевое слово для поиска.
     * @return Список DTO найденных товаров.
     */
    public List<ProductDto> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Ищет товары в заданном диапазоне цен.
     * @param min минимальная цена.
     * @param max максимальная цена.
     * @return Список DTO найденных товаров.
     */
    public List<ProductDto> searchProductsByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min, max).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // --- Приватные методы-мапперы ---

    /**
     * Преобразует сущность Product в ProductDto.
     * @param product Сущность для преобразования.
     * @return DTO.
     */
    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        return dto;
    }

    /**
     * Преобразует ProductDto в сущность Product.
     * @param dto DTO для преобразования.
     * @return Сущность.
     */
    private Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        return product;
    }
}