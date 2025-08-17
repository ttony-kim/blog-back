package project.blog.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.member.entity.Member;
import project.blog.domain.member.repository.MemberRepository;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;
import project.blog.global.config.common.BCryptEncryptor;

//@Component
@RequiredArgsConstructor
public class InitDB {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final BCryptEncryptor bCryptEncryptor;

//    @PostConstruct
//    @Transactional
    public void init() {
        Category category1 = Category.of("category1", true, 1);
        Category category2 = Category.of("category2", true,2);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        for(int i = 0; i < 10; i ++) {
            Post post;
            if(( i + 1 ) % 2 == 0 ) {
                post = Post.of("Title " + (i + 1),
                            "Content: " + (i + 1) + " 입니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!zzz!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                                    category1);
            } else {
                post = Post.of("Title " + (i + 1),
                            "Content: " + (i + 1) + " 입니다.",
                                category2);
            }
            postRepository.save(post);
        }

        Member member = Member.of("test", bCryptEncryptor.encryptPassword("test"), "test");
        memberRepository.save(member);
    }
}
