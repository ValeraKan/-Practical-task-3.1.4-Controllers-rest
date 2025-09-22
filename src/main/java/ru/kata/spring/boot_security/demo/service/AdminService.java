package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDTO;

import java.util.List;

public interface AdminService {

    List<UserDTO> getAllUsers();

    UserDTO saveUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    UserDTO getUser(Long id);

    void deleteUser(Long id);
}