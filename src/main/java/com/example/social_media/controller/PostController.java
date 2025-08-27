package com.example.social_media.controller;

import com.example.social_media.models.post.PostRequest;
import com.example.social_media.models.post.PostResponse;
import com.example.social_media.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public PostResponse create(@RequestBody @Valid PostRequest request) {
        return postService.create(request);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

    @PatchMapping("/{postId}")
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public PostResponse updateText(@PathVariable Long postId, @RequestParam("text") String text) {
        return postService.updateText(postId, text);
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
