package project.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity getAllPost() {
        log.info("Method: getAllPost");

        Map<String, Object> result = new HashMap<>();
        result.put("list", postService.getAllPost());
        result.put("count", postService.getAllPostCount());

        return ResponseEntity.ok(result);
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
}
