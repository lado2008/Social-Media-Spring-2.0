package com.example.social_media.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.social_media.models.comment.CommentRequest;
import com.example.social_media.models.comment.CommentResponse;
import com.example.social_media.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public CommentResponse create(@RequestBody @Valid CommentRequest request) {
        return commentService.create(request);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

    @PatchMapping("/{commentId}")
    @PreAuthorize("hasAuthority('admin') or isAuthenticated()")
    public CommentResponse updateText(@PathVariable Long commentId, @RequestParam("text") String text) {
        return commentService.updateText(commentId, text);
    }

    @GetMapping("/post/{postId}")
    public Page<CommentResponse> getAllByPost(@PathVariable Long postId, @RequestParam int page,
            @RequestParam int size) {
        return commentService.getAllByPost(postId, page, size);
    }
}