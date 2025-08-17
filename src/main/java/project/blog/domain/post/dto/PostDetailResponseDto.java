package project.blog.domain.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.attachment.dto.AttachmentDto;
import project.blog.domain.category.dto.CategoryResponseDto;
import project.blog.domain.post.entity.Post;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private String createdDate;
    private CategoryResponseDto category;
    private List<AttachmentDto> attachments;

    public PostDetailResponseDto(Long id, String title, String content, String createdDate, CategoryResponseDto category, List<AttachmentDto> attachments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.category = category;
        this.attachments = attachments;
    }

    public static PostDetailResponseDto toDto(Post post) {
        CategoryResponseDto categoryDto = null;
        if (post.getCategory() != null) {
            categoryDto = CategoryResponseDto.from(post.getCategory());
        }

        List<AttachmentDto> attachmentDtos = post.getAttachments() == null ? Collections.emptyList() :
                post.getAttachments().stream()
                        .map(AttachmentDto::from)
                        .collect(Collectors.toList());

        return new PostDetailResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDate().toString(),
                categoryDto,
                attachmentDtos
        );
    }

}
