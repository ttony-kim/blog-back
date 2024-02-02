package project.blog.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.blog.global.config.BasicCode;
import project.blog.domain.post.dto.PostDto;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Page<PostDto> getPosts(Long categoryId, Pageable pageable) {
        Page<Post> posts;

        if(categoryId == null) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByCategoryId(categoryId, pageable);
        }

        return posts.map(PostDto::toDto);
    }

    public void savePost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        return PostDto.toDto(post);
    }

    @Transactional
    public void updatePost(long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        post.update(postDto.getTitle(), postDto.getContent());
    }

    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    public Long getPostCountByCategory(Long categoryId) {
        if(categoryId.equals(BasicCode.ALL.getId())) {
            return postRepository.count();
        }

        return postRepository.countByCategoryId(categoryId);
    }

}
