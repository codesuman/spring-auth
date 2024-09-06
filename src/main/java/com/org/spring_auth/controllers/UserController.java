package com.org.spring_auth.controllers;

import com.org.spring_auth.dto.UserDTO;
import com.org.spring_auth.dto.UserLoginRequest;
import com.org.spring_auth.dto.UserLoginResponse;
import com.org.spring_auth.model.User;
import com.org.spring_auth.services.JwtService;
import com.org.spring_auth.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/api/auth/public/register")
    public User register(@RequestBody UserDTO userDTO) {
        return this.userService.register(User.builder().username(userDTO.getUsername()).password(userDTO.getPassword()).build());
    }

    @PostMapping("/api/auth/public/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));

            // if there is no exception thrown from authentication manager,
            // we can generate a JWT token and give it to user.
            if(authentication.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(userLoginRequest.getUsername(), userLoginRequest.getPassword(), List.of()));
                return ResponseEntity.ok(new UserLoginResponse(userLoginRequest.getUsername(), jwtToken, "Login Success"));
            } else
                throw new BadCredentialsException("Incorrect username or password");
        } catch (Exception e){
            log.error("Exception occurred while Login > createAuthenticationToken ", e);
            return new ResponseEntity<>(new UserLoginResponse("", "", "Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }
    }
}
