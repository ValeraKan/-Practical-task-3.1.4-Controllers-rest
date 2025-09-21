package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(r -> new RoleDTO(r.getId(), r.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getEmail())); // если нужно закодировать пароль
        }
        adminService.saveUser(user);
        return convertToDTO(user);
    }

    @PutMapping("/users/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User existingUser = adminService.getUser(id);
        if (existingUser != null) {
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            // Обновление ролей
            Set<Role> roles = userDTO.getRoles().stream()
                    .map(roleService::findRoleByName) // нужен метод поиска роли по имени
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
            adminService.saveUser(existingUser);
            return convertToDTO(existingUser);
        }
        return null;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User with id " + id + " successfully deleted"
            ));
        } catch (EmptyResultDataAccessException e) { // если юзер не найден
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "User with id " + id + " not found"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error deleting user"
            ));
        }
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User user = adminService.getUser(id);
        return user != null ? convertToDTO(user) : null;
    }

    private UserDTO convertToDTO(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), roleNames);
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        // роли будут назначены в updateUser
        return user;
    }
}