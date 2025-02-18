package com.shopme.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.entities.Role;
import com.shopme.entities.User;
import com.shopme.services.RoleService;
import com.shopme.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private RoleService roleService;
        
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Model เป็น interface ที่ใช้ในการส่งข้อมูลไปยัง view
    @GetMapping
    public String viewIndexPage(Model model) {
        List<User> users = userService.findAll();

        // addAttribute() ใช้ในการส่งข้อมูลไปยัง view โดยที่ข้อมูลที่ส่งไปจะถูกเก็บไว้ใน model ซึ่งเป็นตัวแปรที่ใช้ในการเก็บข้อมูลที่จะส่งไปยัง view
        model.addAttribute("users", users);

        return "users/index";
    }

    @GetMapping("/create")
    public String viewCreatePage(Model model) {
        // 
        User user = new User();
        model.addAttribute("user", user);

        Iterable<Role> roles = this.roleService.findAll();
        model.addAttribute("roles", roles);

        return "users/user_form";
    }

    @PostMapping("/create")
    public String createUser(User user, RedirectAttributes redirectAttributes) {
        System.out.println(user.toString());
        this.userService.save(user);

        redirectAttributes.addFlashAttribute("message", "The user has been successfully");

        return "redirect:/users";
    }

}
