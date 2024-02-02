package project.blog.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.domain.post.entity.Post;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String createdDate;

    @Builder
    public PostDto(Long id, String title, String content, String createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
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
                .createdDate(post.getCreatedDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

}
