package project.blog.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.blog.domain.category.dto.CategoryDto;
import project.blog.domain.category.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::toDto)
                .collect(Collectors.toList());
    }

}
