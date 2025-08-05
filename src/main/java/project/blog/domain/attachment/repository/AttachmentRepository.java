package project.blog.domain.attachment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.blog.domain.attachment.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
