
package com.example.social_media.controller;

import com.example.social_media.models.user.UserRequest;
import com.example.social_media.models.user.UserResponse;
import com.example.social_media.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        return userService.register(userRequest, userRequest.getRole());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String token = userService.login(username, password);
        return Map.of("token", token);
    }

    @GetMapping("/{username}")
    public UserResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PatchMapping("/deactivate/{username}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deactivateUser(@PathVariable String username) {
        userService.deactivateUser(username);
        return ResponseEntity.ok().build();
    }
}