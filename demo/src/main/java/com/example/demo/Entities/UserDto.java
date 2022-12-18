package com.example.demo.Entities;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class UserDto {

    @Id
    @Size(min=3, max=30)
    private String username;

    @Size(min=1, max=30)
    private String firstName;

    @Size(min=1, max=30)
    private String lastName;

    @Size(min=3, max=30)
    private String password;

    private String role = "USER";


    public UserDto() {
    }

    public UserDto(String username, String firstName, String lastName, String password, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
