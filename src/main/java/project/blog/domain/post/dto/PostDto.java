package project.blog.domain.post.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;
import project.blog.domain.post.entity.Post;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private String createdDate;

    private PostDto(Long id, String title, String content, String categoryName, String createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.createdDate = createdDate;
    }

    public static PostDto from(Post post) {
        return new PostDto(post.getId(),
                            post.getTitle(),
                            post.getContent(),
                            post.getCategory().getName(),
                            post.getCreatedDate().toString());
    }

    public Post toEntity(Category category) {
        return Post.from(title, content, category);
    }

}
