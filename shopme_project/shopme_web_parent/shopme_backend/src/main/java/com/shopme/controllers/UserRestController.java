package com.shopme.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // @Param ใช้ในการรับค่า parameter ที่ส่งมาจาก client ผ่าน URL เช่น /check_email?email=xxx
    @PostMapping("/check_email_unique")
    public String checkEmailUnique(@RequestParam("email") String email, @RequestParam(value = "id", required = false) Integer id) {

        return this.userService.isEmailUnique(email, id) ? "OK" : "Duplicated";
    }

}
