package project.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.blog.dto.CategoryDto;
import project.blog.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        log.info("Method: getAllCategories");

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

}
