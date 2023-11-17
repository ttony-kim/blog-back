package project.blog.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.blog.entity.Post;
import project.blog.repository.PostRepository;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        for(int i = 0; i < 10; i ++) {
            Post post = Post.builder()
                    .title("Title " + (i + 1))
                    .content("Content: " + (i + 1) + " 입니다.")
                    .build();
            postRepository.save(post);
        }
    }
}
