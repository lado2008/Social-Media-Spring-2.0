package com.example.social_media.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.social_media.models.comment.CommentRequest;
import com.example.social_media.models.comment.CommentResponse;
import com.example.social_media.service.CommentService;
import com.example.social_media.service.UserService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
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
    public CommentResponse create(@RequestParam("username") String username,
            @RequestBody @Valid CommentRequest request) {
        validateUsername(username);
        return commentService.create(username, request);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@RequestParam("username") String username, @PathVariable Long commentId) {
        validateUsername(username);
        commentService.delete(username, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentResponse updateText(@RequestParam("username") String username, @PathVariable Long commentId,
            @RequestParam("text") String text) {
        validateUsername(username);
        return commentService.updateText(username, commentId, text);
    }

    @GetMapping("/post/{postId}")
    public Page<CommentResponse> getAllByPost(@PathVariable Long postId, @RequestParam int page,
            @RequestParam int size) {
        return commentService.getAllByPost(postId, page, size);
    }
}