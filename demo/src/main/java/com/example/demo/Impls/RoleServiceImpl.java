package com.example.demo.Impls;

import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public Role findRole(String role){
        return roleRepository.findByRole(role);
    }
}
