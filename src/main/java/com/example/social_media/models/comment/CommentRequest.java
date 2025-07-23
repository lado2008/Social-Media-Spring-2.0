package com.example.social_media.models.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Comment text cannot be empty")
    @Size(min = 2, max = 128, message = "Comment text must be 2-128 characters")
    private String text;

    @NotNull(message = "Post ID cannot be null")
    private Long postId;

    // ...existing code...
}