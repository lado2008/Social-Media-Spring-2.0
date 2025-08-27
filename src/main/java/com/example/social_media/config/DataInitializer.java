package com.example.social_media.config;

import com.example.social_media.models.user.RoleEntity;
import com.example.social_media.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("admin").isEmpty()) {
                roleRepository.save(new RoleEntity(null, "admin"));
            }
            if (roleRepository.findByName("user").isEmpty()) {
                roleRepository.save(new RoleEntity(null, "user"));
            }
        };
    }
}
