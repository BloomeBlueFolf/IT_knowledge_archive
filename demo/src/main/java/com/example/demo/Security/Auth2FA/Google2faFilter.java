package com.example.demo.Security.Auth2FA;

import com.example.demo.Impls.UserServiceImpl;
import com.example.demo.Security.User;
import com.example.demo.Security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Component
public class Google2faFilter extends GenericFilterBean {

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();
    private final RequestMatcher urlIs2fa = new AntPathRequestMatcher("/user/verify2FA");
    private final RequestMatcher urlResource = new AntPathRequestMatcher("/resources/**");

    @Autowired
    private UserServiceImpl userService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        StaticResourceRequest.StaticResourceRequestMatcher staticResourceRequestMatcher =
                PathRequest.toStaticResources().atCommonLocations();

        if(urlIs2fa.matches(request) || urlResource.matches(request) ||
                staticResourceRequestMatcher.matcher(request).isMatch()){
            filterChain.doFilter(request, response);
            return;
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if(authentication != null && !authenticationTrustResolver.isAnonymous(authentication)){


            if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof UserPrincipal){


                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

                User user = userService.findUser(userPrincipal.getUsername());
                if(user.getUseMFA() && user.getGoogle2FaRequired()){

                    google2faFailureHandler.onAuthenticationFailure(request, response, null);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
