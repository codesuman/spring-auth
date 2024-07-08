package com.org.spring_auth.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            if(authRequest.getUsername() == null || authRequest.getPassword() == null)
                throw new AuthenticationException("AuthRequest is empty");

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // Assuming successful authentication, you would normally return a JWT token here
            return "Login successful!";
        } catch (AuthenticationException e) {
            return "Login failed!";
        }
    }
}

@Data
@NoArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}