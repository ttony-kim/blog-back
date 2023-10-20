package project.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.blog.dto.PostDto;
import project.blog.service.PostService;

import java.util.HashMap;
import java.util.List;
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
}
