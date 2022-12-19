package com.example.demo.Controllers;

import com.example.demo.Entities.UserDto;
import com.example.demo.Repositories.RoleRepository;
import com.example.demo.Security.User;
import com.example.demo.Services.UserService;
import com.example.demo.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {


    @Autowired
    private UserService userService;

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

        userService.saveUser(newUser);
        return "redirect:/user/folders";
    }

    @GetMapping("/user/showProfile")
    public String showUserProfile(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute(("user"), userService.findUser(auth.getName()));
        return "userprofile";
    }

    @GetMapping("/showCredits")
    public String showCredits(){

        return "credits";
    }

    @GetMapping("/admin/showAccounts")
    public String showAccounts(Model model){

        model.addAttribute(("accounts"), userService.showAllUsers());
        return "accounts";
    }

}
