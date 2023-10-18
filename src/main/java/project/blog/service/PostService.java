package project.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.dto.PostDto;
import project.blog.entity.Post;
import project.blog.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<PostDto> getAllPost() {
        return postRepository.findAll().stream()
                .map(Post::toPostDto)
                .collect(Collectors.toList());
    }


}
