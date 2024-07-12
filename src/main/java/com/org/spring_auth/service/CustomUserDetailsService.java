package com.org.spring_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> usersMap = new HashMap<>();
        usersMap.put("ricky", passwordEncoder.encode("123"));
        usersMap.put("martin", passwordEncoder.encode("456"));

        if (usersMap.containsKey(username)) {
            return new User(username, usersMap.get(username), new ArrayList<>());
        }

        // If this is thrown, then we won't generate a JWT token.
        throw new UsernameNotFoundException(username + " not found.");
    }
}