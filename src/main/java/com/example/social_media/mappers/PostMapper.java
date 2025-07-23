package com.example.social_media.mappers;

import com.example.social_media.models.post.PostEntity;
import com.example.social_media.models.post.PostRequest;
import com.example.social_media.models.post.PostResponse;

public class PostMapper {

    public static PostEntity mapRequestToEntity(PostRequest request) {
        PostEntity entity = new PostEntity();
        entity.setText(request.getText());
        return entity;
    }

    public static PostResponse mapEntityToResponse(PostEntity entity) {
        return new PostResponse(entity.getId(), entity.getText(), entity.getUser().getUsername());
    }
}
