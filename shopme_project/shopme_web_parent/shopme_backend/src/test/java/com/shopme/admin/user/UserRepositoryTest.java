package com.shopme.admin.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.entities.Role;
import com.shopme.entities.User;
import com.shopme.repositories.UserRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    // TestEntityManager ใช้สำหรับการทดสอบ Entity ใน JPA
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void testCreateUser() {
        Role roleAdmin = entityManager.find(Role.class, 2);

        User user = new User("root@gmail.com", "root", "Root", "Rosetta");
        user.addRole(roleAdmin);

        User savedUser = this.repo.save(user);
        assertTrue(savedUser.getId() > 0);
    }

    @Test
    public void testAddRoleToNewUser() {
        Role roleSale = entityManager.find(Role.class, 3);

        User user = new User("member@gmail.com", "member", "Member", "Rosetta");
        user.addRole(roleSale);

        User savedUser = this.repo.save(user);

        assertTrue(savedUser.getId() > 0);
    }

    @Test 
    public void testListAllUsers() {
        Iterable<User> users = this.repo.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User user = this.repo.findById(1).get();
        System.out.println(user);
        assertTrue(user != null);
    }

    @Test 
    public void testUpdateUserDetails() {
        User user = this.repo.findById(1).get();
        user.setEnabled(true);

        User userSaved = this.repo.save(user);
        assertTrue(userSaved.isEnabled());
    }

    @Test 
    public void testUpdateUserRoles() {
        User user = this.repo.findById(2).get();
        Role roleSale = entityManager.find(Role.class, 3);
        user.getRoles().remove(roleSale);

        Role roleEditor = entityManager.find(Role.class, 4);
        user.addRole(roleEditor);

        User userSaved = this.repo.save(user);
        assertTrue(userSaved.getRoles().size() == 1);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "root@gmail.com";

        Optional<User> userOptinal = this.repo.findByEmail(email);

        assertTrue(userOptinal.isPresent()); // ตรวจสอบว่ามีข้อมูลหรือไม่ ถ้ามีจะ return true
    }

    @Test
    public void testCountById() {
        Integer id = 6;
        Long countById = this.repo.countById(id);

        assertTrue(countById == 1);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 5;

        // Pageable ใช้สำหรับการทำ pagination ในการดึงข้อมูลจาก database
        // PageRequest ใช้สำหรับการสร้าง Pageable object
        // of() ใช้สำหรับการสร้าง Pageable object โดยที่จะรับ parameter 2 ตัวคือ pageNumber และ pageSize
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Page ใช้สำหรับการเก็บข้อมูลที่ได้จากการทำ pagination
        Page<User> page = this.repo.findAll(pageable);
        
        // getContent() ใช้สำหรับการดึงข้อมูลที่ได้จากการทำ pagination
        List<User> users = page.getContent();

        for (User user : users) {
            System.out.println(user.toString());
        }

        assertTrue(users.size() <= pageSize && users.size() > 0);
    }

    @Test 
    public void testSearchUsers() {
        String keyword = "user01";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = this.repo.findAll(keyword, pageable);

        List<User> users = page.getContent();

        users.forEach(user -> System.out.println(user));

        assertTrue(users.size() > 0);
    }
}
