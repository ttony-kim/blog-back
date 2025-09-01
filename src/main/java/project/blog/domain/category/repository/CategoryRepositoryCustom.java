package project.blog.domain.category.repository;

import project.blog.domain.category.dto.CategoryDetailResponseDto;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<CategoryDetailResponseDto> findCategoryDetails();

}
