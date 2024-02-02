package project.blog.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.category.entity.Category;
import project.blog.domain.post.entity.Post;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.post.repository.PostRepository;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    @Transactional
    public void init() {
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        for(int i = 0; i < 10; i ++) {
            Post post;
            if(( i + 1 ) % 2 == 0 ) {
                post = Post.builder()
                        .title("Title " + (i + 1))
                        .content("Content: " + (i + 1) + " 입니다.")
                        .category(category1)
                        .build();
            } else {
                post = Post.builder()
                        .title("Title " + (i + 1))
                        .content("Content: " + (i + 1) + " 입니다.")
                        .category(category2)
                        .build();
            }
            postRepository.save(post);
        }
    }
}
