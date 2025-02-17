package com.shopme.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopme.entities.User;
import com.shopme.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Model เป็น interface ที่ใช้ในการส่งข้อมูลไปยัง view
    @GetMapping
    public String viewIndex(Model model) {
        List<User> users = userService.findAll();

        // addAttribute() ใช้ในการส่งข้อมูลไปยัง view โดยที่ข้อมูลที่ส่งไปจะถูกเก็บไว้ใน model ซึ่งเป็นตัวแปรที่ใช้ในการเก็บข้อมูลที่จะส่งไปยัง view
        model.addAttribute("users", users);

        return "users/index";
    }

}
