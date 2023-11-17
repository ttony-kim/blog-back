package project.blog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.entity.Post;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;

    @Builder
    public PostDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // dto -> entity
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    // entity -> dto
    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
