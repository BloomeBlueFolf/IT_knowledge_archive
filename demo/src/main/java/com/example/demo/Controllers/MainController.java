package com.example.demo.Controllers;

import com.example.demo.Security.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){

        return "index";
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @PostMapping("/")
    public String validateLoginInfo(Model model,
                                    @Valid User user,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        model.addAttribute("user", user.getUsername());
        return "welcome";
    }
}
