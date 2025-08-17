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

}
