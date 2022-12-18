package com.example.demo.Security.Config;

import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.Role;
import com.example.demo.Security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){

        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        roleRepository.saveAll(List.of(adminRole, userRole));

        if(!userRepository.existsByUsername("admin1")) {

            User admin1 = new User("default", "default", "admin1", passwordEncoder.encode("admin1"), List.of(adminRole, userRole));
            User user1 = new User("default", "default", "user1", passwordEncoder.encode("user1"), List.of(userRole));

            List<User> users = List.of(admin1, user1);

            userRepository.saveAll(users);
        }
    }
}