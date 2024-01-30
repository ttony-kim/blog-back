package project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
