package com.trackhire.trackhire.security;

import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.exception.ResourceNotFoundException;
import com.trackhire.trackhire.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email));
    }
}