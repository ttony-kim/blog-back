package project.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.dto.PostDto;
import project.blog.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<PostDto> getAllPost() {
        return postRepository.findAll().stream()
                .map(PostDto::toDto)
                .collect(Collectors.toList());
    }

    public long getAllPostCount() {
        return postRepository.count();
    }

    public void savePost(PostDto postDto) {
        postRepository.save(postDto.toEntity());
    }

}
