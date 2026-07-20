package com.trackhire.trackhire.service;

import com.trackhire.trackhire.dto.LoginRequest;
import com.trackhire.trackhire.dto.LoginResponse;
import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.exception.DuplicateResourceException;
import com.trackhire.trackhire.exception.InvalidCredentialsException;
import com.trackhire.trackhire.exception.ResourceNotFoundException;
import com.trackhire.trackhire.repository.UserRepository;
import com.trackhire.trackhire.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicateResourceException("Email already registered: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
}