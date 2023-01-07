package com.example.demo.Security.Auth2FA;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.User;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CredentialRepository implements ICredentialRepository {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getSecretKey(String username) {
        return userRepository.findByUsername(username).getSecret();
    }

    @Override
    public void saveUserCredentials(String username,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {

        User user = userRepository.findByUsername(username);
        user.setSecret(secretKey);
        user.setUseMFA(true);
        userRepository.save(user);
    }
}
