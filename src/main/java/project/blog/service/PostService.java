package project.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.blog.dto.PostDto;
import project.blog.entity.Post;
import project.blog.repository.PostRepository;

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

    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        return PostDto.toDto(post);
    }

    @Transactional
    public void updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        post.update(postDto.getTitle(), postDto.getContent());
    }

    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

}
