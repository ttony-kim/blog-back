package project.blog.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "게시글 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주세요.")
    private String content;

    @NotNull(message = "카테고리 ID를 입력해 주세요.")
    private Long categoryId;

    private List<MultipartFile> files;
    private List<Long> deletedFileIds;

    public Post toEntity(Category category) {
        return Post.of(title, content, category);
    }

}
