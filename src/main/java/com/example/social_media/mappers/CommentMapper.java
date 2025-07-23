package com.example.social_media.mappers;

import com.example.social_media.models.comment.CommentEntity;
import com.example.social_media.models.comment.CommentRequest;
import com.example.social_media.models.comment.CommentResponse;

public class CommentMapper {
    public static CommentEntity mapRequestToEntity(CommentRequest commentRequest) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(commentRequest.getText());

        return commentEntity;
    }

    public static CommentResponse mapEntityToResponse(CommentEntity commentEntity) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(commentEntity.getId());
        commentResponse.setText(commentEntity.getText());
        commentResponse.setPostId(commentEntity.getPost().getId());
        commentResponse.setUsername(commentEntity.getUser().getUsername());

        return commentResponse;
    }
}