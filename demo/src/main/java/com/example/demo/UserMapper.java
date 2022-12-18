package com.example.demo;

import com.example.demo.Entities.UserDto;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Security.Role;
import com.example.demo.Security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public UserDto toDto(User user) {
        String username = user.getUsername();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String password = user.getPassword();

        String role = user.getRoles().get(0).getRole();

        return new UserDto(username, firstName, lastName, password, role);
    }

    public User toUser(UserDto userDto) {

        Role role = roleRepository.findByRole(userDto.getRole());
        List<Role> roleList = new LinkedList<>();
        roleList.add(role);
        roleRepository.save(role);
        return new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(), encoder.encode(userDto.getPassword()), roleList);
    }
}
