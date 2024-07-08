package com.org.spring_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final Map<String, String> usersMap = new HashMap<>();

    @Autowired
    public CustomUserDetailsService() {
        // Initialize the usersMap with encoded passwords
        usersMap.put("ricky", "$2a$10$Dow1SE9N1XzFxXh3YDJIoO/b0Zi4DlCg8Up7X5DpIS9b/T/xkaHOO"); // password: 123
        usersMap.put("martin", "$2a$10$Dow1SE9N1XzFxXh3YDJIoO/b0Zi4DlCg8Up7X5DpIS9b/T/xkaHOO"); // password: 456
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (usersMap.containsKey(username)) {
            return new User(username, usersMap.get(username), new ArrayList<>()); // Empty authorities list
        }

        // If this is thrown, then we won't generate a JWT token.
        throw new UsernameNotFoundException(username + " not found.");
    }
}