package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> getAllRoles();

    Role saveRole(Role role);

    void deleteRoleById(Long id);

    Object findRoleById(Long id);

    Role findRoleByName(String name);

    Optional<Role> getRoleById(Long id);
}