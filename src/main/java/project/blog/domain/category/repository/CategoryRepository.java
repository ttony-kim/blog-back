package project.blog.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    List<Category> findAllByEnabledTrueOrderByDisplayOrderAsc();

}
