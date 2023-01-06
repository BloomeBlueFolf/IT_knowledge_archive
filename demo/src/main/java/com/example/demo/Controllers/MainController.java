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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class MainController {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;


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

        List<User> userList = userService.showAllUsers();
        for(User user : userList){
            if(user.getUsername().equals(userDto.getUsername())){
                return "redirect:/admin/addAccount?usernameExists";
            }
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

        model.addAttribute(("auth"), SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute(("accounts"), userService.showAllUsers());
        return "accounts";
    }

    @GetMapping("/admin/deleteAccount")
    public String deleteAccount(@RequestParam ("username") String username){

        userService.deleteUser(userService.findUser(username));
        return "redirect:/admin/showAccounts";
    }

    @GetMapping("/user/editAccount")
    public String editAccount(@RequestParam ("username") String username,
                              Model model){

        model.addAttribute(("userDto"), userMapper.toDto(userService.findUser(username)));
        return "editAccountForm";
    }

    @PostMapping("/user/editAccount")
    public String editAccount(@Valid @ModelAttribute ("userDto") UserDto userDto,
                              BindingResult result,
                              Model model){

        if(result.hasErrors()){

            model.addAttribute(("userDto"), userDto);
            return "editAccountForm";
        }

        User editedUser = userService.findUser(userDto.getUsername());

        editedUser.setFirstName(userDto.getFirstName());
        editedUser.setLastName(userDto.getLastName());
        editedUser.setPassword(encoder.encode(userDto.getPassword()));

        userService.saveUser(editedUser);

        return "redirect:/user/showProfile";
    }

    @GetMapping("/admin/editAccount")
    public String editAccountAdmin(@RequestParam ("username") String username,
                              Model model){

        model.addAttribute(("userDto"), userMapper.toDto(userService.findUser(username)));
        model.addAttribute(("username"), username);
        return "editAccountFormAdmin";
    }

    @PostMapping("/admin/editAccount")
    public String editAccountAdmin(@Valid @ModelAttribute ("userDto") UserDto userDto,
                              BindingResult result,
                              Model model,
                              @RequestParam ("username") String username){

        if(result.hasErrors()){

            model.addAttribute(("userDto"), userDto);
            return "editAccountFormAdmin";
        }

        User editedUser = userService.findUser(userDto.getUsername());

        editedUser.setFirstName(userDto.getFirstName());
        editedUser.setLastName(userDto.getLastName());
        editedUser.setPassword(encoder.encode(userDto.getPassword()));

        userService.saveUser(editedUser);
        return "redirect:/admin/showAccounts";
    }
}
