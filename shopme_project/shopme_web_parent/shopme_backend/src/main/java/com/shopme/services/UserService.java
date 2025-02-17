package com.shopme.services;

import java.util.List;

import com.shopme.entities.User;

public interface UserService {
    List<User> findAll();
}
