package com.org.spring_auth.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/test")
    public String test() {
        return "Testing GET in Auth Ctrl.";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            if(authRequest.getUsername() == null || authRequest.getPassword() == null)
                throw new AuthenticationException("AuthRequest is empty");

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