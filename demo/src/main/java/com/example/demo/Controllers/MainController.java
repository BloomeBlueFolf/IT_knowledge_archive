package com.example.demo.Controllers;

import com.example.demo.Entities.UserDto;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.User;
import com.example.demo.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @GetMapping("/admin/addAccount")
    public String addUser(Model model) {

        UserDto userDto = new UserDto();
        model.addAttribute(("userDto"), userDto);
        return "addAccountForm";
    }

    @PostMapping("/admin/addAccount")
    public String addUser(@Valid @ModelAttribute ("userDto") UserDto userDto,
                          BindingResult result) {

        if(result.hasErrors()){
            return "addAccountForm";
        }

        User newUser = userMapper.toUser(userDto);

        userRepository.save(newUser);
        return "redirect:/user/folders";
    }
}
