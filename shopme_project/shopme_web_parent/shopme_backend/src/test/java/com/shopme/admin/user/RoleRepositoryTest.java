package com.shopme.admin.user;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.entities.Role;
import com.shopme.repositories.RoleRepository;

@DataJpaTest // ใช้สำหรับการทดสอบ Repository โดยจะทำการสร้าง Bean ที่เกี่ยวข้องกับ JPA ขึ้นมาเพื่อทำการทดสอบ
@AutoConfigureTestDatabase(replace = Replace.NONE) // ใช้สำหรับการกำหนดว่าจะใช้ Database จริงหรือใช้ H2 Database ในการทดสอบ ในที่นี้ใช้ NONE คือใช้ Database จริง
@Rollback(false) // ใช้สำหรับการกำหนดว่าจะทำการ Rollback ข้อมูลหลังจากการทดสอบหรือไม่ ในที่นี้ใช้ false คือไม่ทำ Rollback
public class RoleRepositoryTest {

    @Autowired // ใช้สำหรับการ Inject Bean ที่เราต้องการทดสอบเข้ามา
    RoleRepository roleRepository;

    @Test 
    public void testCreateFirstRole() {
       Role roleAdmin = new Role("Admin", "Manage everything"); 

       Role savedRole = this.roleRepository.save(roleAdmin);
       
       assertNotEquals(null, savedRole);
    }

    @Test 
    public void testCreateRestRoles() {
        Role roleSalesperson = new Role("Salesperson", "Manage product price, customers, shipping, orders and sales report");
        Role roleEditor = new Role("Editor", "Manage categories, brands, products, articles and menus");
        Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
        Role roleAssistant = new Role("Assistant", "Manage questions and reviews");

        this.roleRepository.save(roleSalesperson);
        this.roleRepository.save(roleEditor);
        this.roleRepository.save(roleShipper);
        this.roleRepository.save(roleAssistant);
    }
}
