package project.blog.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.blog.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            select p from Post p left join fetch p.category
            where (:categoryId is null or p.category.id = :categoryId)
            and (:searchValue is null or (p.title like concat('%', :searchValue, '%') or p.content like concat('%', :searchValue, '%')))
            order by p.createdDate desc
            """,
           countQuery = """
            select count(p) from Post p
            where (:categoryId is null or p.category.id = :categoryId)
            and (:searchValue is null or (p.title like concat('%', :searchValue, '%') or p.content like concat('%', :searchValue, '%')))
            """
    )
    Page<Post> findPostsByCategoryAndKeyword(@Param("categoryId") Long categoryId, @Param("searchValue") String searchValue, Pageable pageable);

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
