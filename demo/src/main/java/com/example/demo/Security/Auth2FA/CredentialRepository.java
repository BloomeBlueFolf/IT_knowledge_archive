package com.example.demo.Security.Auth2FA;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.Auth2FA.Try.UserTOTP;
import com.example.demo.Security.User;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CredentialRepository implements ICredentialRepository {

    //private final Map<String, UserTOTP> usersKeys = new HashMap<String, UserTOTP>();

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getSecretKey(String username) {
        //return usersKeys.get(username).getSecretKey();
        return userRepository.findByUsername(username).getSecret();
    }

    @Override
    public void saveUserCredentials(String username,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        //usersKeys.put(username, new UserTOTP(username, secretKey, validationCode, scratchCodes));
        User user = userRepository.findByUsername(username);
        user.setSecret(secretKey);
        user.setUseMFA(true);
        userRepository.save(user);
    }

//    public UserTOTP getUser(String username) {
//        return usersKeys.get(username);
//    }
}
