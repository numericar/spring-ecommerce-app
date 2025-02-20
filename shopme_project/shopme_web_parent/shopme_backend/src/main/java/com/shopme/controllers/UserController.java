package com.shopme.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.entities.Role;
import com.shopme.entities.User;
import com.shopme.services.FileService;
import com.shopme.services.RoleService;
import com.shopme.services.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private RoleService roleService;
    private FileService fileService;
    
    public UserController(UserService userService, RoleService roleService, FileService fileService) {
        this.userService = userService;
        this.roleService = roleService;
        this.fileService = fileService;
    }

    // Model เป็น interface ที่ใช้ในการส่งข้อมูลไปยัง view
    @GetMapping
    public String viewIndexPage(Model model) {
        // List<User> users = userService.findAll();

        // addAttribute() ใช้ในการส่งข้อมูลไปยัง view โดยที่ข้อมูลที่ส่งไปจะถูกเก็บไว้ใน
        // model ซึ่งเป็นตัวแปรที่ใช้ในการเก็บข้อมูลที่จะส่งไปยัง view
        // model.addAttribute("users", users);

        Optional<String> sortFieldOptional = Optional.empty();
        Optional<String> sortDirOptional = Optional.empty();
        Page<User> page = userService.findAll(0, sortFieldOptional, sortDirOptional);
        List<User> users = page.getContent();

        model.addAttribute("users", users);

        return "users/index";
    }

    @GetMapping("/page/{pageNumber}")
    public String viewPaginatedPage(
        @PathVariable("pageNumber") int pageNumber,
        @RequestParam(name = "sortField", required = false) Optional<String> sortField,
        @RequestParam(name = "sortDir", required = false) Optional<String> sortDir,
        Model model) {
        if (pageNumber < 1) {
            return "redirect:/users/pages/1";
        }

        pageNumber -= 1;

        Page<User> page = userService.findAll(pageNumber, sortField, sortDir);
        List<User> users = page.getContent();

        // หา startCount โดยการหาหน้าปัจจุบันคูณกับขนาดข้อมูลที่ต้องการแสดง
        // startCount จะใช้ในการแสดงข้อมูลว่าเริ่มที่ข้อมูลที่เท่าไหร่
        long startCount = pageNumber * UserService.SIZE + 1; // หาว่าเริ่มที่ข้อมูลที่เท่าไหร่
        long endCount = startCount + UserService.SIZE - 1; // 
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        
        model.addAttribute("startCount", startCount); // ข้อมูลเริ่มต้น
        model.addAttribute("endCount", endCount); // ข้อมูลสุดท้าย
        model.addAttribute("totalItems", page.getTotalElements()); // จำนวนข้อมูลทั้งหมด
        model.addAttribute("totalPages", page.getTotalPages()); // จำนวนหน้าทั้งหมด
        model.addAttribute("currentPage", pageNumber + 1); // หน้าปัจจุบัน
        model.addAttribute("users", users);
        model.addAttribute("sortField", sortField.orElse(null));
        model.addAttribute("sortDir", sortDir.orElse("asc"));

        return "users/index";
    }

    @GetMapping("/create")
    public String viewCreatePage(Model model) {
        //
        User user = new User();
        model.addAttribute("user", user);

        Iterable<Role> roles = this.roleService.findAll();
        model.addAttribute("roles", roles);

        model.addAttribute("pageTitle", "Create new user");

        return "users/user_form";
    }

    // MultiPartFile ใช้ในการรับข้อมูลจาก form ที่มีการ upload file
    @PostMapping("/create")
    public String createUser(User user, RedirectAttributes redirectAttributes, @RequestParam("userPhoto") MultipartFile userPhoto) {
        // ถ้า userPhoto ไม่ว่างเปล่า
        if (!userPhoto.isEmpty()) {
            // ถ้า user มี id แสดงว่าเป็นการ edit user ให้ทำการลบรูปเก่าออก
            if (user.getId() != null) {
                User userExist = this.userService.findById(user.getId()).get();
                String oldPhoto = userExist.getPhotos();
                this.fileService.remove("userPhotos", oldPhoto);
            }

            String fileName = StringUtils.cleanPath(userPhoto.getOriginalFilename()); // ใช้ในการดึงชื่อไฟล์จาก MultipartFile โดยที่ cleanPath จะทำการลบ path ที่อาจจะมีอยู่ในชื่อไฟล์ออก
            String outDir = "userPhotos";
    
            this.fileService.save(outDir, fileName, userPhoto);
            user.setPhotos(fileName);
        }

        this.userService.save(user);

        redirectAttributes.addFlashAttribute("message", "The user has been successfully");

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String viewEditUserPage(Model model, @PathVariable("id") int id) {
        try {
            Optional<User> userOptional = this.userService.findById(id);

            if (userOptional.isEmpty()) {
                // NoSuchElementException ใช้ในการ handle exception ที่เกิดขึ้นเมื่อไม่พบข้อมูลที่ต้องการ
                throw new NoSuchElementException("Could not find any user with ID " + id);
            }

            User user = userOptional.get();
            model.addAttribute("user", user);

            Iterable<Role> roles = this.roleService.findAll();
            model.addAttribute("roles", roles);

            model.addAttribute("pageTitle", "Edit user id: " + id);

            return "users/user_form";
        } catch (NoSuchElementException ex) {
            // throw new UserNotFoundException("Could not find any user with ID " + id);

            return "redirect:/users";
        }
    }

    @PostMapping("/edit")
    public String editUser(User user, RedirectAttributes redirectAttributes) {
        this.userService.save(user);

        redirectAttributes.addFlashAttribute("message", "The user has been updated successfully");

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        this.userService.delete(id);

        redirectAttributes.addFlashAttribute("message", "The user has been deleted successfully");

        return "redirect:/users";
    }
}
