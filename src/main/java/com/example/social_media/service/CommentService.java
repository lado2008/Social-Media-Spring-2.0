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

    public CommentResponse create(String username, CommentRequest request) {
        UserEntity user = userService.findByUsername(username);
        PostEntity post = postService.getPostEntityById(request.getPostId());
        CommentEntity entity = new CommentEntity();
        entity.setText(request.getText());
        entity.setPost(post);
        entity.setUser(user);
        CommentEntity saved = commentRepository.save(entity);
        return CommentMapper.mapEntityToResponse(saved);
    }

    public void delete(String username, Long commentId) {
        CommentEntity comment = getCommentByIdAndUsername(commentId, username);
        commentRepository.delete(comment);
    }

    public CommentResponse updateText(String username, Long commentId, String newText) {
        CommentEntity comment = getCommentByIdAndUsername(commentId, username);
        comment.setText(newText);
        CommentEntity saved = commentRepository.save(comment);
        return CommentMapper.mapEntityToResponse(saved);
    }

    public Page<CommentResponse> getAllByPost(Long postId, int page, int size) {
        return commentRepository.findAllByPost_Id(postId, PageRequest.of(page, size, Sort.Direction.DESC, "id"))
                .map(CommentMapper::mapEntityToResponse);
    }

    private CommentEntity getCommentByIdAndUsername(Long commentId, String username) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if (!comment.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this comment");
        }
        return comment;
    }

    public CommentEntity getCommentEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }
}
