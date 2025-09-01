package project.blog.domain.category.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.blog.domain.category.entity.Category;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryRequestDto {

    @Valid
    private List<CategoryDto> categories;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CategoryDto {

        private Long id;

        @NotBlank(message = "카테고리 명을 입력해 주세요.")
        private String name;

        @NotNull(message = "카테고리 사용여부를 입력해 주세요.")
        private Boolean enabled;

        @NotNull(message = "카테고리 순서를 입력해 주세요.")
        @Min(value = 0, message = "카테고리 순서는 최소 0 이상으로 입력해 주세요.")
        private Integer displayOrder;

        public Category toEntity() {
            return Category.of(name, enabled, displayOrder);
        }

    }

}
