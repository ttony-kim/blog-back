package project.blog.domain.attachment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import project.blog.domain.attachment.entity.Attachment;
import project.blog.domain.attachment.repository.AttachmentRepository;
import project.blog.domain.file.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final FileService fileService;
    private final AttachmentRepository attachmentRepository;

    public ResponseEntity<Resource> downloadAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("attachment doesn't exist" ));
        Resource resource = fileService.getFileResource(attachment.getFilePath()  +"\\" + attachment.getSavedFileName());
        String encodedFileName = UriUtils.encode(attachment.getOriginalFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    public List<Attachment> getAttachments(List<Long> attachmentIds) {
        return attachmentRepository.findAllById(attachmentIds);
    }

}
