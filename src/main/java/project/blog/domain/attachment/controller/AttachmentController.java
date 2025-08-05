package project.blog.domain.attachment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.blog.domain.attachment.service.AttachmentService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachment")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/download/{attachmentId}" )
    public ResponseEntity<Resource> downloadAttachment(@PathVariable("attachmentId") long attachmentId) {
        log.info("Method: downloadAttachment");

        return attachmentService.downloadAttachment(attachmentId);
    }

}
