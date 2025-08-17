package project.blog.domain.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.blog.domain.category.dto.CategoryDetailResponseDto;
import project.blog.domain.category.dto.CategoryRequestDto;
import project.blog.domain.category.dto.CategoryResponseDto;
import project.blog.domain.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        log.info("Method: getCategories");

        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/detail")
    public ResponseEntity<List<CategoryDetailResponseDto>> getCategoryDetails() {
        log.info("Method: getCategoryDetails");

        return ResponseEntity.ok(categoryService.getCategoryDetails());
    }

    @PostMapping
    public ResponseEntity<String> saveCategories(@RequestBody List<CategoryRequestDto> categories) {
        log.info("Method: saveCategories");

        categoryService.saveCategories(categories);

        return ResponseEntity.ok("ok");
    }


}
