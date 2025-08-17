package project.blog.domain.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;

@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    private Long id;
    private String name;
    private Boolean enabled;
    private Integer displayOrder;

    public Category toEntity() {
        return Category.of(name, enabled, displayOrder);
    }

}
