package project.blog.domain.attachment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.blog.domain.post.entity.Post;
import project.blog.global.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "tbl_attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseTimeEntity {

    @Id
    @Column(name = "attachment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String savedFileName;

    private String contentType;

    private String extension;

    private Long size;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Attachment(String originalFileName, String savedFileName, String contentType, String extension, Long size, String filePath) {
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.contentType = contentType;
        this.extension = extension;
        this.size = size;
        this.filePath = filePath;
    }

    public static Attachment of(String originalFileName, String savedFileName, String contentType, String extension, Long size, String filePath) {
        return new Attachment(originalFileName,
                savedFileName,
                contentType,
                extension,
                size,
                filePath);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "filePath='" + filePath + '\'' +
                ", size=" + size +
                ", extension='" + extension + '\'' +
                ", contentType='" + contentType + '\'' +
                ", savedFileName='" + savedFileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", id=" + id +
                '}';
    }

}
