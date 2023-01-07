package com.example.demo.Security.Auth2FA;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserRepository userRepository;

    public CustomLogoutSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        User user = userRepository.findByUsername(authentication.getName());
        user.setGoogle2FaRequired(true);
        userRepository.save(user);
        response.sendRedirect("/login");
    }
}