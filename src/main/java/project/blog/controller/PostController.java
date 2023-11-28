package project.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.blog.dto.PostDto;
import project.blog.service.PostService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ResponseEntity<Page<PostDto>> getAllPost(Pageable pageable) {
        log.info("Method: getAllPost");

        return ResponseEntity.ok(postService.getAllPost(pageable));
    }

    @PostMapping("/api/posts")
    public ResponseEntity<String> savePost(@RequestBody PostDto postDto) {
        log.info("Method: savePost");

        postService.savePost(postDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/api/posts/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") long id) {
        PostDto result = postService.getPost(id);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/posts/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") long id, @RequestBody PostDto postDto) {
        log.info("Method: updatePost");

        postService.updatePost(id, postDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        log.info("Method: deletePost");

        postService.deletePost(id);

        return ResponseEntity.ok("ok");
    }
}
