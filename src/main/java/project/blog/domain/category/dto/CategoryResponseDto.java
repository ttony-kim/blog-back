package project.blog.domain.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String name;

    private CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }

}
