package project.blog.domain.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private Long postCount;

    @Builder
    public CategoryDto(Long id, String name, Long postCount) {
        this.id = id;
        this.name = name;
        this.postCount = postCount;
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
