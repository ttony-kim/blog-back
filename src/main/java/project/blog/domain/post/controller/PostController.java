package project.blog.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.blog.domain.post.dto.PostDetailResponseDto;
import project.blog.domain.post.dto.PostListResponseDto;
import project.blog.domain.post.dto.PostRequestDto;
import project.blog.domain.post.service.PostService;
import project.blog.global.config.common.ErrorCode;
import project.blog.global.exception.custom.BadRequestException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostListResponseDto>> getPosts(Long categoryId, String searchValue, Pageable pageable) {
        log.info("Method: getPosts");
        Page<PostListResponseDto> posts = postService.getPosts(categoryId, searchValue, pageable);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<String> savePost(@ModelAttribute @Valid PostRequestDto postDto) {
        log.info("Method: savePost");
        postService.savePost(postDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable("postId") long postId) {
        log.info("Method: getPost");
        PostDetailResponseDto result = postService.getPost(postId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") long postId, @ModelAttribute @Valid PostRequestDto postDto) {
        log.info("Method: updatePost");
        postService.updatePost(postId, postDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") long postId) {
        log.info("Method: deletePost");
        postService.deletePost(postId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getPostCount(Long categoryId, String searchValue) {
        log.info("Method: getPostCount");
        if (categoryId == null && (searchValue == null || searchValue.isBlank())) {
            throw new BadRequestException(ErrorCode.INVALID_CATEGORY_OR_SEARCH);
        }

        return ResponseEntity.ok(postService.getPostCount(categoryId, searchValue));
    }

}
