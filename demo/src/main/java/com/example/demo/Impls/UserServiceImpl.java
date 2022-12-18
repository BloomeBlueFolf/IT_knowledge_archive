package com.example.demo.Impls;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.User;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }
}
