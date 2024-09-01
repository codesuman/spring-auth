package com.org.spring_auth.services;

import com.org.spring_auth.model.User;
import com.org.spring_auth.model.UserPrincipal;
import com.org.spring_auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userModel = this.userRepo.findByUsername(username);

        if(userModel == null) {
            String errMsg = "User not found";

            System.out.println(errMsg);
            throw new UsernameNotFoundException(errMsg);
        }

        return new UserPrincipal(userModel);
    }
}
