package project.blog.domain.attachment.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;
import project.blog.domain.attachment.entity.Attachment;
import project.blog.domain.attachment.repository.AttachmentRepository;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;
import project.blog.global.config.common.ErrorCode;
import project.blog.support.TestJwtTokenProvider;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestJwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        token = jwtTokenProvider.getToken();

        attachmentRepository.deleteAll();
        postRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("첨부파일 다운로드")
    @Transactional
    void downloadAttachment_success() throws Exception {
        Category category = categoryRepository.save(Category.of("Category", true, 1));

        byte[] fileContent = "Hello World".getBytes();
        MockMultipartFile file = new MockMultipartFile("files", "text.txt", MediaType.TEXT_PLAIN_VALUE, fileContent);

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                    .formParam("categoryId", category.getId())
                    .multiPart("files", "text.txt", file.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(200)
                    .body(equalTo("ok"));

        List<Post> all = postRepository.findAll();
        Post savedPost = all.get(0);
        Attachment savedAttachment = savedPost.getAttachments().get(0);

        MockHttpServletResponse response = RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .get("/api/attachment/download/{attachmentId}", savedAttachment.getId())
                    .andReturn()
                    .getMockHttpServletResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeader("Content-Type")).isEqualTo(savedAttachment.getContentType());
        assertThat(response.getHeader("Content-Disposition"))
                .contains("attachment; filename=" + UriUtils.encode(savedAttachment.getOriginalFileName(), StandardCharsets.UTF_8));

        byte[] responseContent = response.getContentAsByteArray();
        assertThat(responseContent).isEqualTo(fileContent);

        Path filePath = Paths.get(savedAttachment.getFilePath(), savedAttachment.getSavedFileName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    @Test
    @DisplayName("첨부파일 다운로드 - 첨부파일이 존재하지 않을 경우")
    void downloadAttachment_fail_attachmentNotFound() {
        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .get("/api/attachment/download/{attachmentId}", 9999L)
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.ATTACHMENT_NOT_FOUND.name()))
                    .body("message", equalTo(ErrorCode.ATTACHMENT_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("첨부파일 다운로드 - 잘못된 ID 타입")
    void downloadAttachment_fail_invalidAttachmentIdType() {
        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .get("/api/attachment/download/{attachmentId}", "test")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.name()))
                    .body("message", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.getMessage()));
    }

}