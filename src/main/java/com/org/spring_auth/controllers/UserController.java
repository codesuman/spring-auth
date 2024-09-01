package com.org.spring_auth.controllers;

import com.org.spring_auth.dto.UserDTO;
import com.org.spring_auth.model.User;
import com.org.spring_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/auth/public/register")
    public User register(@RequestBody UserDTO userDTO) {
        return this.userService.register(User.builder().username(userDTO.getUsername()).password(userDTO.getPassword()).build());
    }
}
