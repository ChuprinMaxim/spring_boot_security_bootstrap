package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImp userService;
    @Autowired
    public AdminController(UserServiceImp userService) {
        this.userService = userService;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @GetMapping()
    public String pageAdminPanel(@AuthenticationPrincipal User client, Model model, @ModelAttribute("user") User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("client", client);
        model.addAttribute("roles", userService.getRoles());
        return "admin";
    }

    @PostMapping("/adduser")
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("delete") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
