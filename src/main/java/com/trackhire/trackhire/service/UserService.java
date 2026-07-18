package com.trackhire.trackhire.service;

import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.exception.DuplicateResourceException;
import com.trackhire.trackhire.exception.ResourceNotFoundException;
import com.trackhire.trackhire.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // TODO: replace with a custom DuplicateEmailException once exception handling is added
    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicateResourceException("Email already registered: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    // TODO: replace null return with a custom NotFoundException once exception handling is added
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}