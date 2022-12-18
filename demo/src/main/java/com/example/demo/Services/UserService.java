package com.example.demo.Services;

import com.example.demo.Security.User;

import java.util.Optional;

public interface UserService {

    public void saveUser(User user);

    public User findUser(String username);
}
