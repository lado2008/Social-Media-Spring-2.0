package com.example.social_media.models.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank(message = "Post text cannot be empty")
    @Size(min = 2, max = 512, message = "Post text must be 2-512 characters")
    private String text;
}
