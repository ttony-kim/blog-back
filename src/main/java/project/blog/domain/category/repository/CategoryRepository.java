package project.blog.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
