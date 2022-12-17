package com.example.demo.Repositories;

import com.example.demo.Security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    public Boolean existsByUsername(String username);
}