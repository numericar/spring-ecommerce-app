package com.shopme.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.entities.User;
import com.shopme.repositories.UserRepository;

@Service // ใช้สำหรับระบุว่า class นี้เป็น Service class ที่ใช้ในการจัดการ business logic
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public static final int SIZE = 5;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        // (List<User>) ใช้ในการ cast ค่าที่ได้จาก method findAll() ให้กลายเป็น List<User> โดยที่ method findAll() จะ return ค่าเป็น Iterable<User>
        return (List<User>) userRepo.findAll();
    }

    @Override
    public void save(User user) {
        boolean isUpdateUser = (user.getId() != null);
        if (isUpdateUser) {
            User existingUser = this.userRepo.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                String encodedPassword = this.passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }
        } else {
            String encodedPassword = this.passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        
        // save ใช้ในการบันทึกข้อมูลลงใน database 
        this.userRepo.save(user);
    }

    @Override
    public boolean isEmailUnique(String email, Integer id) {
        Optional<User> userOptional = this.userRepo.findByEmail(email);

        // isEmpty() ใช้ในการตรวจสอบว่า Optional นั้นมีค่าว่างหรือไม่ ถ้ามีค่าว่างจะ return true ถ้าไม่มีค่าว่างจะ return false
        if (userOptional.isEmpty()) {
            return true;
        }

        User user = userOptional.get();
        if (user.getId().equals(id)) { // ถ้า id ของ user ที่มีอยู่ใน database ตรงกับ id ที่ส่งมาจาก client ให้ return true
            return true;
        }

        return false; 
    }

    @Override
    public Optional<User> findById(Integer id) {
        return this.userRepo.findById(id);
    }

    @Override
    public void delete(Integer id) {
        Long count = this.userRepo.countById(id);
        
        if (count == null || count == 0) {
            throw new RuntimeException("Could not find any user with ID " + id);
        }

        this.userRepo.deleteById(id);
    }

    @Override
    public Page<User> findAll(int page) {
        // Pageable คือ interface ที่ใช้ในการทำการ paginate ข้อมูล
        // PageRequest คือ class ที่ใช้ในการสร้าง instance ของ Pageable
        Pageable pageable = PageRequest.of(page, SIZE);

        return this.userRepo.findAll(pageable);
    }

    @Override
    public Page<User> findAll(int page, Optional<String> sortFieldOptional, Optional<String> sortDirOptional, Optional<String> keyword) {
        String sortField = "id";
        if (sortFieldOptional.isPresent()) {
            sortField = sortFieldOptional.get();
        }

        // Sort คือ class ที่ใช้ในการ sort ข้อมูล
        // Sort.by() ใช้ในการสร้าง instance ของ Sort
        Sort sort = Sort.by(sortField); // ใช้ในการ sort ข้อมูลโดยที่ sortField คือ field ที่ต้องการให้ sort

        // ascending คือ เรียงจาก น้อยไปมาก
        // descending คือ เรียงจาก มากไปน้อย
        // Optional ใช้ในการ handle ค่าที่อาจจะเป็น null หรือไม่มีค่า
        if (sortDirOptional.isPresent() && sortDirOptional.get().equals("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, SIZE, sort); // ใช้ในการสร้าง instance ของ Pageable โดยที่ sort ข้อมูลด้วย sort ที่กำหนดไว้

        if (keyword.isPresent()) {
            return this.userRepo.findAll(keyword.get(), pageable);
        }

        return this.userRepo.findAll(pageable);
    }
}
