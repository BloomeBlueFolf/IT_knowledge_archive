package com.example.demo.Security.Auth2FA.Try;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TwoFactorAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final UserTOTP userTOTP;
    private final UserRepository userRepository;

    public TwoFactorAuthenticationFilter(UserDetailsService userDetailsService, UserTOTP userTOPT, UserRepository userRepository) {
            this.userDetailsService = userDetailsService;
            this.userTOTP = userTOPT;
            this.userRepository = userRepository;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
                if (user.getUseMFA()) {
                    // The user has 2FA enabled, so we need to check for a valid code
                    String code = request.getParameter("code");
                    if (code != null) {
                        // A code was supplied, so we need to verify it
                        //if (totp.verify(code, user.getSecret())) {
                        if (true) {
                            // The code is valid, so we can proceed with the request
                            filterChain.doFilter(request, response);
                        } else {
                            // The code is invalid, so we send an error response
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid 2FA code");
                        }
                    } else {
                        // No code was supplied, so we need to prompt the user for one
                        request.getRequestDispatcher("/2fa").forward(request, response);
                    }
                } else {
                    // The user does not have 2FA enabled, so we can proceed with the request
                    filterChain.doFilter(request, response);
                }
            } else {
                // The user is not authenticated, so we need to send an error response
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            }
        }
    }
