package com.example.demo.Security.Auth2FA.Try;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTOTP {

    private String username;

    private String secretKey;

    private int validationCode;

    private List<Integer> scratchCodes;


    public UserTOTP() {
    }

    public UserTOTP(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
        this.username = username;
        this.secretKey = secretKey;
        this.validationCode = validationCode;
        this.scratchCodes = scratchCodes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public List<Integer> getScratchCodes() {
        return scratchCodes;
    }

    public void setScratchCodes(List<Integer> scratchCodes) {
        this.scratchCodes = scratchCodes;
    }
}
