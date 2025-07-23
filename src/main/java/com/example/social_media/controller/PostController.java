package com.example.social_media.controller;

import com.example.social_media.models.post.PostRequest;
import com.example.social_media.models.post.PostResponse;
import com.example.social_media.service.PostService;
import com.example.social_media.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    private void validateUsername(String username) {
        if (username == null || !userService.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid or missing username");
        }
    }

    @PostMapping
    public PostResponse create(@RequestParam("username") String username, @RequestBody @Valid PostRequest request) {
        validateUsername(username);
        return postService.create(username, request);
    }

    @DeleteMapping("/{postId}")
    public void delete(@RequestParam("username") String username, @PathVariable Long postId) {
        validateUsername(username);
        postService.delete(username, postId);
    }

    @PatchMapping("/{postId}")
    public PostResponse updateText(@RequestParam("username") String username, @PathVariable Long postId,
            @RequestParam("text") String text) {
        validateUsername(username);
        return postService.updateText(username, postId, text);
    }

    @GetMapping
    public Page<PostResponse> getAll(@RequestParam int page, @RequestParam int size) {
        return postService.getAll(page, size);
    }

    @GetMapping("/user/{username}")
    public Page<PostResponse> getAllByUser(@PathVariable String username, @RequestParam int page,
            @RequestParam int size) {
        return postService.getAllByUser(username, page, size);
    }

    @GetMapping("/{postId}")
    public PostResponse getById(@PathVariable Long postId) {
        return postService.getById(postId);
    }
}
