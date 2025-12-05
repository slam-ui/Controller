package com.example.controller;

import com.example.controller.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ищем клиента по email
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        // Превращаем нашего Customer в Spring User
        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword()) // Здесь должен быть зашифрованный пароль
                .roles(customer.getRole())        // Например, "USER" или "ADMIN"
                .build();
    }
}