package com.shopme.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Page คือ interface ที่ใช้ในการเก็บข้อมูลที่จะ return กลับไปให้ client โดยที่ข้อมูลที่จะ return จะถูกแบ่งเป็นหลายหน้า
    Page<User> findAll(Pageable page);

    // %?1% ใช้ในการระบุว่าเราต้องการให้ค่าที่เป็น parameter ที่ 1 นั้นอยู่ในตำแหน่งไหนของคำ query
    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public Optional<User> findByEmail(@Param("email") String email);

    public Long countById(Integer id);
}
