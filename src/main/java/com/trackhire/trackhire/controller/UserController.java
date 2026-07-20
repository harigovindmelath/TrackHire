package com.trackhire.trackhire.controller;

import com.trackhire.trackhire.dto.LoginRequest;
import com.trackhire.trackhire.dto.LoginResponse;
import com.trackhire.trackhire.entity.User;
import com.trackhire.trackhire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }
}