package com.example.social_media.controller;

import com.example.social_media.models.user.UserRequest;
import com.example.social_media.models.user.UserResponse;
import com.example.social_media.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        return userService.register(userRequest);
    }

    @GetMapping("/{username}")
    public UserResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
}