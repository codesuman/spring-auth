package com.org.spring_auth.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
