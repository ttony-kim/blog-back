package project.blog.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.category.dto.CategoryResponseDto;
import project.blog.domain.post.entity.Post;

@Getter
@NoArgsConstructor
public class PostListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String createdDate;
    private CategoryResponseDto category;

    public PostListResponseDto(Long id, String title, String content, String createdDate, CategoryResponseDto category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.category = category;
    }

    public static PostListResponseDto toDto(Post post) {
        CategoryResponseDto categoryDto = null;
        if (post.getCategory() != null) {
            categoryDto = CategoryResponseDto.from(post.getCategory());
        }

        return new PostListResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toString(),
                categoryDto
        );
    }

}
