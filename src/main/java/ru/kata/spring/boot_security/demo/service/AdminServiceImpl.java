package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            RoleService roleService,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getEmail()));
        }
        userRepository.save(user);
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow();
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());

        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleService::findRoleByName)
                .collect(Collectors.toSet());

        existingUser.setRoles(roles);
        userRepository.save(existingUser);

        return convertToDTO(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    // 🔽 Приватные мапперы
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
        return user;
    }
}


