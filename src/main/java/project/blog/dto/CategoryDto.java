package project.blog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.blog.entity.Category;

@Data
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;

    @Builder
    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
