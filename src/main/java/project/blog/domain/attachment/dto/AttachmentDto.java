package project.blog.domain.attachment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.attachment.entity.Attachment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDto {

    private Long id;
    private String name;
    private Long size;

    public AttachmentDto(Long id, String name, Long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public static AttachmentDto from(Attachment attachment) {
        return new AttachmentDto(attachment.getId(),
                attachment.getOriginalFileName(),
                attachment.getSize()
        );
    }

}
