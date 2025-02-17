package com.shopme.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.entities.User;
import com.shopme.repositories.UserRepository;

@Service // ใช้สำหรับระบุว่า class นี้เป็น Service class ที่ใช้ในการจัดการ business logic
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> findAll() {
        // (List<User>) ใช้ในการ cast ค่าที่ได้จาก method findAll() ให้กลายเป็น List<User> โดยที่ method findAll() จะ return ค่าเป็น Iterable<User>
        return (List<User>) userRepo.findAll();
    }
}
