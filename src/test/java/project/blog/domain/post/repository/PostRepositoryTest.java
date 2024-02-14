package project.blog.domain.post.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.post.entity.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    public void getPostListAll() {
        List<Post> all = postRepository.findAll();
        for (Post post : all) {
            System.out.println("post = " + post);
            System.out.println("post.getCategory() = " + post.getCategory());
        }
    }

    @Test
    public void getPostListByCategoryId_isNull_fetchjoin_Paging() {
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<Post> all = postRepository.findFetchJoinPaging(null, pageRequest);

        for (Post post : all) {
            System.out.println("post = " + post);
            System.out.println("post.getCategory() = " + post.getCategory());
        }
    }

    @Test
    public void getPostListByCategoryId_isNotNull_fetchjoin_Paging() {
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<Post> all = postRepository.findFetchJoinPaging(1L, pageRequest);

        for (Post post : all) {
            System.out.println("post = " + post);
            System.out.println("post.getCategory() = " + post.getCategory());
        }
    }

}