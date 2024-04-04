package project.blog.domain.category.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {

    private Long id;
    private String name;
    private Long postCount;

    private CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
