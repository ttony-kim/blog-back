package project.blog.domain.category.dto;

import lombok.*;
import project.blog.domain.category.entity.Category;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {

    private Long id;
    private String name;

    private CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

}
