package project.blog.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.blog.domain.post.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.category where (:categoryId is null or p.category.id = :categoryId)")
    Page<Post> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    Long countByCategoryId(Long categoryId);

}
