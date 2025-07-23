package com.example.social_media.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public PostResponse create(String username, PostRequest request) {
        UserEntity user = userService.findByUsername(username);
        PostEntity entity = PostMapper.mapRequestToEntity(request);
        entity.setUser(user);
        PostEntity saved = postRepository.save(entity);
        return PostMapper.mapEntityToResponse(saved);
    }

    public void delete(String username, Long postId) {
        PostEntity post = getPostByIdAndUsername(postId, username);
        postRepository.delete(post);
    }

    public PostResponse updateText(String username, Long postId, String newText) {
        PostEntity post = getPostByIdAndUsername(postId, username);
        post.setText(newText);
        PostEntity saved = postRepository.save(post);
        return PostMapper.mapEntityToResponse(saved);
    }

    public Page<PostResponse> getAll(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size)).map(PostMapper::mapEntityToResponse);
    }

    public Page<PostResponse> getAllByUser(String username, int page, int size) {
        return postRepository.findAllByUser_Username(username, PageRequest.of(page, size))
                .map(PostMapper::mapEntityToResponse);
    }

    public PostResponse getById(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return PostMapper.mapEntityToResponse(post);
    }

    public PostEntity getPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    private PostEntity getPostByIdAndUsername(Long postId, String username) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this post");
        }
        return post;
    }
}
