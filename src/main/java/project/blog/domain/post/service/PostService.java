package project.blog.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.global.config.common.BasicCode;
import project.blog.domain.post.dto.PostDto;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public Page<PostDto> getPosts(Long categoryId, String searchValue, Pageable pageable) {
        Page<Post> posts = postRepository.findByCategoryId(categoryId, searchValue, pageable);

        return posts.map(PostDto::from);
    }

    public void savePost(PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("category doesn't exist"));

        postRepository.save(postDto.toEntity(category));
    }

    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        return  PostDto.from(post);
    }

    @Transactional
    public void updatePost(long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));
        Category category =  categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        post.update(postDto.getTitle(), postDto.getContent(), category);
    }

    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    public Long getPostCount(Long categoryId, String searchValue) {
        // category 선택 시
        if (categoryId != null) {
            if (categoryId.equals(BasicCode.ALL.getId())) {
                return postRepository.count();
            } else {
                return postRepository.countByCategoryId(categoryId);
            }
        }

        // 검색어 searchValue 입력 시
        return postRepository.countBySearchValue(searchValue);
    }

}
