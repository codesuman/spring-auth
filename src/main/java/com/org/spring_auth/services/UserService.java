package com.org.spring_auth.services;

import com.org.spring_auth.model.User;
import com.org.spring_auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User register(User user) {
        return this.userRepo.save(user);
    }
}
