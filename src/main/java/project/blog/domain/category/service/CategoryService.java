package project.blog.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.category.dto.CategoryDetailResponseDto;
import project.blog.domain.category.dto.CategoryRequestDto.CategoryDto;
import project.blog.domain.category.dto.CategoryResponseDto;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories() {

        return categoryRepository.findAllByEnabledTrueOrderByDisplayOrderAsc()
                .stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryDetailResponseDto> getCategoryDetails() {

        return categoryRepository.findCategoryDetails();
    }

    public void saveCategories(List<CategoryDto> categories) {
        Map<Boolean, List<CategoryDto>> partitioned = categories.stream()
                .collect(Collectors.partitioningBy(dto -> dto.getId() == null));

        List<CategoryDto> categoriesToInsert = partitioned.get(true); // id == null
        List<CategoryDto> categoriesToUpdate = partitioned.get(false); // id != null

        // 기존 카테고리 수정 및 삭제
        updateOrDeleteCategories(categoriesToUpdate);
        // 새로운 카테고리 추가
        insertCategories(categoriesToInsert);
    }

    private void insertCategories(List<CategoryDto> categoryDtos) {
        if (categoryDtos == null || categoryDtos.isEmpty()) return;

        List<Category> categories = categoryDtos.stream()
                .map(CategoryDto::toEntity)
                .toList();

        categoryRepository.saveAll(categories);
    }

    private void updateOrDeleteCategories(List<CategoryDto> categoryDtos) {
        List<Category> allCategories = categoryRepository.findAll();
        Map<Long, CategoryDto> dtoMap = categoryDtos.stream()
                .collect(Collectors.toMap(CategoryDto::getId, Function.identity()));

        List<Long> deleteCategoryIds = allCategories.stream()
                .map(Category::getId)
                .filter(id -> !dtoMap.containsKey(id))
                .toList();

        if (!deleteCategoryIds.isEmpty()) {
            deleteCategories(deleteCategoryIds);
        }

        for (Category category : allCategories) {
            CategoryDto categoryDto = dtoMap.get(category.getId());
            if (categoryDto != null) {
                category.update(categoryDto.getName(), categoryDto.getEnabled(), categoryDto.getDisplayOrder());
            }
        }
    }

    private void deleteCategories(List<Long> categoryIds) {
        List<Post> posts = postRepository.findByIdCategoryIdIn(categoryIds);
        postRepository.deleteAll(posts);

        categoryRepository.deleteAllById(categoryIds);
    }

}
