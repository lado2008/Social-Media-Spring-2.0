package com.example.social_media.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.social_media.security.SecurityUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import com.example.social_media.mappers.PostMapper;
import com.example.social_media.models.post.PostEntity;
import com.example.social_media.models.post.PostRequest;
import com.example.social_media.models.post.PostResponse;
import com.example.social_media.models.user.UserEntity;
import com.example.social_media.repositories.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public PostResponse create(PostRequest request) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        PostEntity entity = PostMapper.mapRequestToEntity(request);
        entity.setUser(user);
        PostEntity saved = postRepository.save(entity);
        return PostMapper.mapEntityToResponse(saved);
    }

    public void delete(Long postId) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if (!SecurityUtil.isAdmin(auth)
                && !post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to delete this post");
        }
        postRepository.delete(post);
    }

    public PostResponse updateText(Long postId, String newText) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is deactivated and cannot perform this action");
        }
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if (!SecurityUtil.isAdmin(auth)
                && !post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to edit this post");
        }
        post.setText(newText);
        PostEntity saved = postRepository.save(post);
        return PostMapper.mapEntityToResponse(saved);
    }

    public Page<PostResponse> getAll(int page, int size) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated and cannot view posts");
        }
        return postRepository.findAll(PageRequest.of(page, size)).map(PostMapper::mapEntityToResponse);
    }

    public Page<PostResponse> getAllByUser(String username, int page, int size) {
        Authentication auth = SecurityUtil.getAuth();
        String currentUsername = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(currentUsername);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated and cannot view posts");
        }
        return postRepository.findAllByUser_Username(username, PageRequest.of(page, size))
                .map(PostMapper::mapEntityToResponse);
    }

    public PostResponse getById(Long postId) {
        Authentication auth = SecurityUtil.getAuth();
        String username = SecurityUtil.getCurrentUsername(auth);
        UserEntity user = userService.findByUsername(username);
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated and cannot view posts");
        }
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return PostMapper.mapEntityToResponse(post);
    }

    public PostEntity getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
