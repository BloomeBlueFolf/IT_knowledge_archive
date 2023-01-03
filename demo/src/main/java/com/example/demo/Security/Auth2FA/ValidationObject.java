package com.example.demo.Security.Auth2FA;

public class ValidationObject {

    private String password;

    private String username;

    public ValidationObject() {
    }

    public ValidationObject(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
