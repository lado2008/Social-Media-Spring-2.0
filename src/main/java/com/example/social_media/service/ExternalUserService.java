package com.example.social_media.service;

import com.example.social_media.models.user.UserEntity;
import com.example.social_media.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.social_media.client.ReqresClient;
import com.example.social_media.client.ReqresResponse;
import java.time.LocalDate;

@Component
@Slf4j
public class ExternalUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReqresClient reqresClient;

    @PostConstruct
    public void UsersFromExternalApi() {
        try {
            ReqresResponse response = reqresClient.getUsers(null);
            if (response != null && response.getData() != null) {

                response.getData().stream().forEach(user -> {
                    String local = user.getEmail().split("@")[0];
                    local = local.replaceAll("[^A-Za-z0-9]", "");
                    String username;

                    if (local.length() >= 4 && local.length() <= 16) {
                        username = local;
                    } else if (local.length() > 16) {
                        username = local.substring(0, 16);
                    } else {
                        username = "user" + ((int)(Math.random()*9000)+1000);
                    }
                    if (!userRepository.existsByUsername(username)) {
                        UserEntity entity = new UserEntity();
                        entity.setUsername(username);
                        entity.setFirstName(user.getFirst_name());
                        entity.setLastName(user.getLast_name());
                        entity.setDateOfBirth(LocalDate.now());
                        userRepository.save(entity);
                    }
                });
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @PreDestroy
    public void removeReqresUsers() {
        try {
            ReqresResponse response = reqresClient.getUsers(null);

            if (response != null && response.getData() != null) {

                response.getData().stream().forEach(user -> {
                    String local = user.getEmail().split("@")[0];
                    local = local.replaceAll("[^A-Za-z0-9]", "");
                    String username;
                    
                    if (local.length() >= 4 && local.length() <= 16) {
                        username = local;
                    } else if (local.length() > 16) {
                        username = local.substring(0, 16);
                    } else {
                        username = "user" + ((int)(Math.random()*9000)+1000);
                    }
                    userRepository.findAll().stream()
                        .filter(u -> u.getUsername().equals(username))
                        .forEach(userRepository::delete);
                });
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
