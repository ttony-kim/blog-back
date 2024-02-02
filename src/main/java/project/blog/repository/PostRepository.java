package project.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
    Long countByCategoryId(Long categoryId);

}
