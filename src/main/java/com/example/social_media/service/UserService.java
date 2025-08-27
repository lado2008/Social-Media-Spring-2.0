
package com.example.social_media.service;

import com.example.social_media.security.CustomAuthentication;
import com.example.social_media.security.RoleConstants;
import com.example.social_media.models.user.RoleEntity;
import com.example.social_media.repositories.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.social_media.mappers.UserMapper;
import com.example.social_media.models.user.UserEntity;
import com.example.social_media.models.user.UserRequest;
import com.example.social_media.models.user.UserResponse;
import com.example.social_media.repositories.UserRepository;
import java.util.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String secret = "VERY-SECRET-KEY1231231233333333123";

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserResponse register(UserRequest userRequest, String roleName) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        String normalizedRole = roleName.toLowerCase();
        if (!normalizedRole.equals(RoleConstants.USER) && !normalizedRole.equals(RoleConstants.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role must be 'user' or 'admin'");
        }
        RoleEntity role = roleRepository.findByName(normalizedRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));
        UserEntity userEntity = UserMapper.mapRequestToEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setRoles(Set.of(role));
        userRequest.setRole(normalizedRole);
        UserEntity saved = userRepository.save(userEntity);
        return UserMapper.mapEntityToResponse(saved);
    }

    public UserResponse getByUsername(String username) {
        UserEntity user = findByUsername(username);
        return UserMapper.mapEntityToResponse(user);
    }

    public String login(String username, String password) {
        UserEntity user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        String role = user.getRoles().stream().findFirst().map(RoleEntity::getName).orElse(RoleConstants.USER);
        String token = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("roles", List.of(role))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
        return token;
    }

    public Authentication authentication(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();
        String username = payload.get("username", String.class);
        String role = ((List<?>) payload.get("roles")).get(0).toString();
        return new CustomAuthentication(username, role, true);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void deactivateUser(String username) {
        Authentication auth = com.example.social_media.security.SecurityUtil.getAuth();
        if (!com.example.social_media.security.SecurityUtil.isAdmin(auth)) {
            throw new org.springframework.security.access.AccessDeniedException("Only admins can deactivate users");
        }
        UserEntity user = findByUsername(username);
        user.setActive(false);
        userRepository.save(user);
    }
}