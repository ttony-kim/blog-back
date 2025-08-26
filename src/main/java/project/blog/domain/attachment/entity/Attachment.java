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

    @Column(length = 255, nullable = false)
    private String originalFileName;

    @Column(length = 255, nullable = false)
    private String savedFileName;

    @Column(length = 255, nullable = false)
    private String contentType;

    @Column(length = 50, nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Column(length = 255, nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Attachment(String originalFileName, String savedFileName, String contentType, String extension, Long size, String filePath) {
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

}
