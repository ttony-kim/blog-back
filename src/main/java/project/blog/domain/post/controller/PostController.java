package project.blog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.blog.domain.post.dto.PostDto;
import project.blog.domain.post.service.PostService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(Long categoryId, Pageable pageable) {
        log.info("Method: getPosts");

        return ResponseEntity.ok(postService.getPosts(categoryId, pageable));
    }

    @PostMapping
    public ResponseEntity<String> savePost(@RequestBody PostDto postDto) {
        log.info("Method: savePost");

        postService.savePost(postDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable("postId") long postId) {
        log.info("Method: getPost");

        PostDto result = postService.getPost(postId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") long postId, @RequestBody PostDto postDto) {
        log.info("Method: updatePost");

        postService.updatePost(postId, postDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") long postId) {
        log.info("Method: deletePost");

        postService.deletePost(postId);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/category/{categoryId}/count")
    public ResponseEntity<Long> getPostCountByCategory(@PathVariable Long categoryId) {
        log.info("Method: getPostCountByCategory");

        return ResponseEntity.ok(postService.getPostCountByCategory(categoryId));
    }
}
