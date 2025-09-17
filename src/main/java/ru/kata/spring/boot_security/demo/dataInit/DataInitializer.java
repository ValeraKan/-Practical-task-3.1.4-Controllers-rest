package ru.kata.spring.boot_security.demo.dataInit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Запуск инициализации данных...");

//        logger.info("Очистка существующих данных...");
//        userRepository.deleteAll();
//        roleRepository.deleteAll();
//        logger.info("Существующие данные очищены");
//
//        logger.info("Создание ролей...");
//        Role adminRole = new Role("ROLE_ADMIN");
//        Role userRole = new Role("ROLE_USER");
//        roleRepository.save(adminRole);
//        roleRepository.save(userRole);
//
//        logger.info("Роли созданы: ROLE_ADMIN, ROLE_USER");
//
//        logger.info("Создание пользователя admin...");
//        User admin = new User();
//        admin.setName("admin");
//        admin.setEmail("admin@mail.com");
//        admin.setPassword(passwordEncoder.encode("admin"));
//        admin.addRole(adminRole);
//        User savedAdmin = userRepository.save(admin);
//        logger.info("Пользователь admin создан с ID: {}, email: {}, password hash: {}",
//                savedAdmin.getId(), savedAdmin.getEmail(), "admin");
//
//        logger.info("Создание пользователя user...");
//        User user = new User();
//        user.setName("user");
//        user.setEmail("user@mail.com");
//        user.setPassword(passwordEncoder.encode("user"));
//        user.addRole(userRole);
//        User savedUser = userRepository.save(user);
//        logger.info("Пользователь user создан с ID: {}, email: {}, password hash: {}",
//                savedUser.getId(), savedUser.getEmail(), "user");

        logger.info("Инициализация данных завершена успешно");
    }
}