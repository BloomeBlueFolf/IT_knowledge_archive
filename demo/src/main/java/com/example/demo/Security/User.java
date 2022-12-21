package com.example.demo.Security;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Id
        @Column(nullable = false)
        private String username;

        @Column(nullable = false)
        private String password;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "username"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private List<Role> roles;

        public User(String firstName, String lastName, String username, String password, List<Role> roles) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public User(){};

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
}

