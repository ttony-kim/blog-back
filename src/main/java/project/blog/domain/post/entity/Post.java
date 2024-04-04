package project.blog.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.category.entity.Category;
import project.blog.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue
    private Long id;

    private String title;
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Post(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public static Post from(String title, String content, Category category) {
        return new Post(title, content, category);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
