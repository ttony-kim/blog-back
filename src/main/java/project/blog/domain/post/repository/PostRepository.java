package project.blog.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.blog.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Long countByCategoryId(Long categoryId);

    @Query("""
            select count(p) from Post p
            where p.title like concat('%', :searchValue, '%') or p.content like concat('%', :searchValue, '%')
            """)
    Long countBySearchValue(@Param("searchValue") String searchValue);

    @Query("""
            select p from Post p
            left join fetch p.attachments
            left join fetch p.category
            where p.id = :postId
            """ )
    Optional<Post> findByIdWithAttachments(@Param("postId" ) Long postId);

}
