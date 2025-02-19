package com.shopme.services;

import java.util.List;
import java.util.Optional;

import com.shopme.entities.User;

public interface UserService {
    List<User> findAll();
    void save(User user);
    boolean isEmailUnique(String email, Integer id);
    Optional<User> findById(Integer id);
    void delete(Integer id);
}
