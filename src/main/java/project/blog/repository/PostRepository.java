package project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
