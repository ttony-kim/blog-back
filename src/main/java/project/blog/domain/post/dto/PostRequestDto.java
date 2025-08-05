package project.blog.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.blog.domain.category.entity.Category;
import project.blog.domain.post.entity.Post;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;
    private Long categoryId;
    private List<MultipartFile> files;
    private List<Long> deletedFileIds;

    public Post toEntity(Category category) {
        return Post.of(title, content, category);
    }

    @Override
    public String toString() {
        return "PostRequestDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }

}
