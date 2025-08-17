package project.blog.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import project.blog.domain.post.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> findPostsByCategoryAndKeyword(@Param("categoryId") Long categoryId, @Param("searchValue") String searchValue, Pageable pageable);

    void deleteByCategoryId(List<Long> categoryIds);

}
