package com.example.demo.config;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Starting data initialization...");

        // Создаем пользователей, если их нет
        if (userRepository.count() == 0) {
            createUsers();
        }

        // Создаем категории
        if (categoryRepository.count() == 0) {
            createCategories();
        }

        // Создаем товары
        if (productRepository.count() == 0) {
            createProducts();
        }

        log.info("Data initialization completed!");
    }

    private void createUsers() {
        log.info("Creating users...");

        // Администратор
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("System Administrator");
        admin.setRole(User.Role.ADMIN);
        admin.setIsActive(true);
        userRepository.save(admin);

        // Менеджер
        User manager = new User();
        manager.setUsername("manager");
        manager.setEmail("manager@example.com");
        manager.setPassword(passwordEncoder.encode("manager123"));
        manager.setFullName("Store Manager");
        manager.setRole(User.Role.MANAGER);
        manager.setIsActive(true);
        userRepository.save(manager);

        // Обычный пользователь
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setFullName("Regular User");
        user.setRole(User.Role.USER);
        user.setIsActive(true);
        userRepository.save(user);

        log.info("Created 3 users: admin, manager, user");
    }

    private void createCategories() {
        log.info("Creating categories...");

        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic devices and gadgets");
        categoryRepository.save(electronics);

        Category books = new Category();
        books.setName("Books");
        books.setDescription("Books and literature");
        categoryRepository.save(books);

        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setDescription("Clothes and accessories");
        categoryRepository.save(clothing);

        log.info("Created 3 categories");
    }

    private void createProducts() {
        log.info("Creating products...");

        Category electronics = categoryRepository.findByName("Electronics").orElseThrow();
        Category books = categoryRepository.findByName("Books").orElseThrow();
        Category clothing = categoryRepository.findByName("Clothing").orElseThrow();

        // Электроника
        Product laptop = new Product();
        laptop.setName("Gaming Laptop");
        laptop.setDescription("High-performance gaming laptop with RTX graphics");
        laptop.setPrice(1299.99);
        laptop.setQuantity(15);
        laptop.setCategory(electronics);
        productRepository.save(laptop);

        Product smartphone = new Product();
        smartphone.setName("Smartphone Pro");
        smartphone.setDescription("Latest flagship smartphone");
        smartphone.setPrice(899.99);
        smartphone.setQuantity(25);
        smartphone.setCategory(electronics);
        productRepository.save(smartphone);

        Product headphones = new Product();
        headphones.setName("Wireless Headphones");
        headphones.setDescription("Noise-cancelling wireless headphones");
        headphones.setPrice(249.99);
        headphones.setQuantity(50);
        headphones.setCategory(electronics);
        productRepository.save(headphones);

        // Книги
        Product javaBook = new Product();
        javaBook.setName("Effective Java");
        javaBook.setDescription("Best practices for Java programming");
        javaBook.setPrice(45.99);
        javaBook.setQuantity(30);
        javaBook.setCategory(books);
        productRepository.save(javaBook);

        Product springBook = new Product();
        springBook.setName("Spring Boot in Action");
        springBook.setDescription("Comprehensive guide to Spring Boot");
        springBook.setPrice(39.99);
        springBook.setQuantity(20);
        springBook.setCategory(books);
        productRepository.save(springBook);

        // Одежда
        Product tshirt = new Product();
        tshirt.setName("Cotton T-Shirt");
        tshirt.setDescription("Comfortable cotton t-shirt");
        tshirt.setPrice(19.99);
        tshirt.setQuantity(100);
        tshirt.setCategory(clothing);
        productRepository.save(tshirt);

        Product jeans = new Product();
        jeans.setName("Classic Jeans");
        jeans.setDescription("Stylish classic jeans");
        jeans.setPrice(59.99);
        jeans.setQuantity(75);
        jeans.setCategory(clothing);
        productRepository.save(jeans);

        log.info("Created 7 products");
    }
}