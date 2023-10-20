package project.blog.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.blog.entity.Post;
import project.blog.repository.PostRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        for(int i = 0; i < 10; i ++) {
            Post post = new Post();
            post.setTitle("Title " + (i+1));
            post.setContent("Content: " + (i+1) + " 입니다.");
            post.setCreateDate(LocalDateTime.now());
            postRepository.save(post);
        }
    }
}
