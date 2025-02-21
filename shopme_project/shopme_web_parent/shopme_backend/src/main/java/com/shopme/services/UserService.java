package com.shopme.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.shopme.entities.User;

public interface UserService {
    public static final int SIZE = 5;
    
    List<User> findAll();
    Page<User> findAll(int page);
    Page<User> findAll(int page, Optional<String> sortField, Optional<String> sortDir, Optional<String> keyword);
    void save(User user);
    boolean isEmailUnique(String email, Integer id);
    Optional<User> findById(Integer id);
    void delete(Integer id);
}
