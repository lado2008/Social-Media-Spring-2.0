package com.example.social_media.mappers;

import com.example.social_media.models.user.UserEntity;
import com.example.social_media.models.user.UserRequest;
import com.example.social_media.models.user.UserResponse;

public class UserMapper {
    public static UserEntity mapRequestToEntity(UserRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setDateOfBirth(userRequest.getDateOfBirth());
        return userEntity;
    }

    public static UserResponse mapEntityToResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setUsername(userEntity.getUsername());
        userResponse.setFirstName(userEntity.getFirstName());
        userResponse.setLastName(userEntity.getLastName());
        userResponse.setDateOfBirth(userEntity.getDateOfBirth());
        return userResponse;
    }
}