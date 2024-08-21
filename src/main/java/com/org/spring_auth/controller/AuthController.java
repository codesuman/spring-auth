package com.org.spring_auth.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.org.spring_auth.security.JwtService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth/public")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("UP");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            // if there is no exception thrown from authentication manager,
            // we can generate a JWT token and give it to user.
            String jwt = jwtService.generateToken(new User(authRequest.getUsername(), authRequest.getPassword(), new ArrayList<>()));
            return ResponseEntity.ok(new LoginResponse(authRequest.getUsername(), jwt, "Login Success"));
        } catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>(new LoginResponse("", "", "Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("API yet to be implemented.");
    }
}

@Data
@NoArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginResponse {
    private String username;
    private String token;
    private String msg;
}