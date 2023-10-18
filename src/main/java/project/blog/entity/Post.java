package project.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import project.blog.dto.PostDto;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createDate;

    public PostDto toPostDto() {
        return PostDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
    }

}
