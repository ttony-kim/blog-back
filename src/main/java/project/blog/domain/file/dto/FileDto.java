package project.blog.domain.file.dto;

import lombok.Getter;
import project.blog.domain.attachment.entity.Attachment;

@Getter
public class FileDto {

    private String originalFileName;
    private String savedFileName;
    private String contentType;
    private String extension;
    private Long size;
    private String filePath;

    public FileDto(String originalFileName, String savedFileName, String contentType, String extension, Long size, String filePath) {
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.contentType = contentType;
        this.extension = extension;
        this.size = size;
        this.filePath = filePath;
    }

    public static FileDto of(String originalFileName, String savedFileName, String contentType, String extension, Long size, String filePath) {
        return new FileDto(originalFileName,
                savedFileName,
                contentType,
                extension,
                size,
                filePath);
    }

    public Attachment toEntity() {
        return Attachment.of(originalFileName,
                savedFileName,
                contentType,
                extension,
                size,
                filePath);
    }

}
