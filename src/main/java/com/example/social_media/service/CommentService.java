package com.example.social_media.service;

import com.example.social_media.mappers.CommentMapper;
import com.example.social_media.models.comment.CommentEntity;
import com.example.social_media.models.comment.CommentRequest;
import com.example.social_media.models.comment.CommentResponse;
import com.example.social_media.models.post.PostEntity;
import com.example.social_media.models.user.UserEntity;
import com.example.social_media.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.social_media.security.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public CommentResponse create(CommentRequest request) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        PostEntity post = postService.getPostEntityById(request.getPostId());
        CommentEntity entity = new CommentEntity();
        entity.setText(request.getText());
        entity.setPost(post);
        entity.setUser(user);
        CommentEntity saved = commentRepository.save(entity);
        return CommentMapper.mapEntityToResponse(saved);
    }

    public void delete(Long commentId) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (!SecurityUtil.isAdmin(auth)
                && !comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to delete this comment");
        }
        commentRepository.delete(comment);
    }

    public CommentResponse updateText(Long commentId, String newText) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (!SecurityUtil.isAdmin(auth)
                && !comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to edit this comment");
        }
        comment.setText(newText);
        CommentEntity saved = commentRepository.save(comment);
        return CommentMapper.mapEntityToResponse(saved);
    }

    public Page<CommentResponse> getAllByPost(Long postId, int page, int size) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated and cannot view comments");
        }
        return commentRepository.findAllByPost_Id(postId, PageRequest.of(page, size, Sort.Direction.DESC, "id"))
                .map(CommentMapper::mapEntityToResponse);
    }

    public CommentEntity getCommentEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }
}
