package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.AdminService;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(AdminService adminService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {

        List<User> allUsers = adminService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("newUser", new User());

        return "admin/all-users";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {

        User user = new User();
        model.addAttribute("user", user);
        return "admin/user-info";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@RequestParam(name = "id", required = false) Long id,
                           @RequestParam("name") String name,
                           @RequestParam("email") String email,
                           @RequestParam(required = false) String password,
                           @RequestParam("roles") List<String> roleIds) {

        User user = new User();
        if (id != null) {
            user.setId(id);
        }

        user.setName(name);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        Set<Role> userRoles = roleIds.stream()
                .map(Long::parseLong)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(userRoles);

        adminService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/updateUser")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(name = "roles") List<String> roleIds) {

        User user = adminService.getUser(id);
        user.setName(name);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        Set<Role> userRoles = roleIds.stream()
                .map(Long::parseLong)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(userRoles);

        adminService.saveUser(user);

        return "redirect:/admin";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin";
    }
}
