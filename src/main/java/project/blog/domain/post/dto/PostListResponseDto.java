package project.blog.domain.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.category.dto.CategoryDto;
import project.blog.domain.post.entity.Post;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String createdDate;
    private CategoryDto category;

    public PostListResponseDto(Long id, String title, String content, String createdDate, CategoryDto category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.category = category;
    }

    public static PostListResponseDto toDto(Post post) {
        CategoryDto categoryDto = null;
        if (post.getCategory() != null) {
            categoryDto = CategoryDto.from(post.getCategory());
        }

        return new PostListResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toString(),
                categoryDto
        );
    }

}
