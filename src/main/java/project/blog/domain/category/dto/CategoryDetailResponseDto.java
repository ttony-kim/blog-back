package project.blog.domain.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDetailResponseDto {

    private Long id;
    private String name;
    private Boolean enabled;
    private Integer displayOrder;
    private Integer postCount;

    public CategoryDetailResponseDto(Long id, String name, Boolean enabled, Integer displayOrder, Integer postCount) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.displayOrder = displayOrder;
        this.postCount = postCount;
    }

}
