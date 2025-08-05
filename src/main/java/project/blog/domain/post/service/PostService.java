package project.blog.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.blog.domain.attachment.entity.Attachment;
import project.blog.domain.attachment.service.AttachmentService;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.file.dto.FileDto;
import project.blog.domain.file.service.FileService;
import project.blog.domain.post.dto.PostDetailResponseDto;
import project.blog.domain.post.dto.PostListResponseDto;
import project.blog.domain.post.dto.PostRequestDto;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;
import project.blog.global.config.common.BasicCode;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public Page<PostListResponseDto> getPosts(Long categoryId, String searchValue, Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByCategoryAndKeyword(categoryId, searchValue, pageable);

        return posts.map(PostListResponseDto::toDto);
    }

    public void savePost(PostRequestDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("category doesn't exist"));

        Post post = postDto.toEntity(category);

        // 첨부파일 저장
        addAttachment(post, postDto.getFiles());

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getPost(Long postId) {
        Post post = postRepository.findByIdWithAttachments(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        return  PostDetailResponseDto.toDto(post);
    }

    public void updatePost(long postId, PostRequestDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));
        Category category =  categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));

        post.update(postDto.getTitle(), postDto.getContent(), category);

        // 삭제된 첨부파일 삭제
        List<Long> deletedFileIds = postDto.getDeletedFileIds();
        if (deletedFileIds != null) {
            List<Attachment> deletedAttachments = attachmentService.getAttachments(deletedFileIds);
            for (Attachment attachment : deletedAttachments) {
                post.removeAttachment(attachment); // db 삭제
                fileService.deleteFile(attachment.getSavedFileName()); // 물리 파일 삭제
            }
        }

        // 새로운 첨부파일 추가
        addAttachment(post, postDto.getFiles());
    }

    public void deletePost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post doesn't exist"));
        for(Attachment attachment : post.getAttachments()) {
            fileService.deleteFile(attachment.getSavedFileName());
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Long getPostCount(Long categoryId, String searchValue) {
        // category 선택 시
        if (categoryId != null) {
            if (categoryId.equals(BasicCode.ALL.getId())) {
                return postRepository.count();
            } else {
                return postRepository.countByCategoryId(categoryId);
            }
        }

        // 검색어 searchValue 입력 시
        return postRepository.countBySearchValue(searchValue);
    }

    private void addAttachment(Post post, List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                FileDto fileDto = fileService.saveFile(file);

                post.addAttachment(fileDto.toEntity());
            }
        }
    }

}
