package project.blog.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostDto {

    private Long id;
    private String title;
    private String content;

}
