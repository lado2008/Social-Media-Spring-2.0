package com.example.social_media.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.social_media.mappers.UserMapper;
import com.example.social_media.models.user.UserEntity;
import com.example.social_media.models.user.UserRequest;
import com.example.social_media.models.user.UserResponse;
import com.example.social_media.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findById(Long userId) {
        Optional<UserEntity> byId = userRepository.findById(userId);
        return byId
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserResponse register(UserRequest userRequest) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals(userRequest.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        UserEntity userEntity = UserMapper.mapRequestToEntity(userRequest);
        UserEntity saved = userRepository.save(userEntity);
        return UserMapper.mapEntityToResponse(saved);
    }

    public UserResponse getByUsername(String username) {
        UserEntity user = findByUsername(username);
        return UserMapper.mapEntityToResponse(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}