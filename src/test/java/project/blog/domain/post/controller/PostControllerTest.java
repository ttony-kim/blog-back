package project.blog.domain.post.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.blog.domain.attachment.entity.Attachment;
import project.blog.domain.attachment.repository.AttachmentRepository;
import project.blog.domain.category.entity.Category;
import project.blog.domain.category.repository.CategoryRepository;
import project.blog.domain.post.entity.Post;
import project.blog.domain.post.repository.PostRepository;
import project.blog.global.config.common.ErrorCode;
import project.blog.support.TestJwtTokenProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

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
    @DisplayName("게시글 목록 조회 - 카테고리 ID 조회")
    void getPosts_success_byCategoryId() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post1 = postRepository.save(Post.of("Post title1", "Post content1", category));
        Post post2 = postRepository.save(Post.of("Post title2", "Post content2", category));

        RestAssuredMockMvc
                .given()
                    .param("categoryId", category.getId())
                    .param("page", 0)
                    .param("size", 10)
                .when()
                    .get("/api/posts")
                .then()
                    .statusCode(200)
                    .body("content.size()", equalTo(2));

    }

    @Test
    @DisplayName("게시글 목록 조회 - 검색어 조회")
    void getPosts_success_bySearchValue() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post1 = postRepository.save(Post.of("Post title1", "Post content1", category));
        Post post2 = postRepository.save(Post.of("Post title2", "Post content2", category));

        RestAssuredMockMvc
                .given()
                    .param("searchValue", "title1")
                    .param("page", 0)
                    .param("size", 10)
                .when()
                    .get("/api/posts")
                .then()
                .statusCode(200)
                    .body("content.size()", equalTo(1))
                    .body("content[0].title", equalTo(post1.getTitle()));
    }

    @Test
    @DisplayName("게시글 저장")
    @Transactional
    void savePost_success() throws Exception {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        MockMultipartFile file1 = new MockMultipartFile("files", "text.txt", MediaType.TEXT_PLAIN_VALUE, "text" .getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "image.png", MediaType.IMAGE_PNG_VALUE, "image" .getBytes());

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                    .formParam("categoryId", category.getId())
                    .multiPart("files", "text.txt", file1.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                    .multiPart("files", "image.png", file2.getBytes(), MediaType.IMAGE_PNG_VALUE)
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(200)
                    .body(equalTo("ok"));

        List<Post> all = postRepository.findAll();
        assertThat(all).hasSize(1);

        Post savedPost = all.get(0);
        assertThat(savedPost.getTitle()).isEqualTo("Post title");
        assertThat(savedPost.getContent()).isEqualTo("Post content");
        assertThat(savedPost.getCategory().getId()).isEqualTo(category.getId());

        List<Attachment> attachments = savedPost.getAttachments();
        assertThat(attachments).hasSize(2);

        Attachment textFile = attachments.stream()
                .filter(a -> a.getOriginalFileName().equals("text.txt"))
                .findFirst()
                .orElseThrow();

        assertThat(textFile.getSavedFileName()).isNotBlank();
        assertThat(textFile.getContentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        assertThat(textFile.getExtension()).isEqualTo("txt");
        assertThat(textFile.getSize()).isGreaterThan(0L);

        Attachment imageFile = attachments.stream()
                .filter(a -> a.getOriginalFileName().equals("image.png"))
                .findFirst()
                .orElseThrow();

        assertThat(imageFile.getSavedFileName()).isNotBlank();
        assertThat(imageFile.getContentType()).isEqualTo(MediaType.IMAGE_PNG_VALUE);
        assertThat(imageFile.getExtension()).isEqualTo("png");
        assertThat(imageFile.getSize()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("게시글 저장 - 필수값 누락")
    void savePost_fail_missingRequiredField() {
        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.name()))
                    .body("message", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.getMessage()))
                    .body("errors.categoryId", equalTo("카테고리 ID를 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 저장 - 카테고리가 존재하지 않을 경우")
    void savePost_fail_categoryNotFound() {
        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                    .formParam("categoryId", 9999L)
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.CATEGORY_NOT_FOUND.name()))
                    .body("message", equalTo(ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("게시글 저장 - 허용되지 않은 파일 확장자")
    void savePost_fail_invalidFileExtension() throws Exception {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        MockMultipartFile file = new MockMultipartFile("files", "exeFile.exe", MediaType.APPLICATION_OCTET_STREAM_VALUE, "exe" .getBytes());

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                    .formParam("categoryId", category.getId())
                    .multiPart("files", "exeFile.exe", file.getBytes(), MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.NOT_ALLOWED_FILE_EXTENSION.name()))
                    .body("message", equalTo(ErrorCode.NOT_ALLOWED_FILE_EXTENSION.getMessage()));
    }

    @Test
    @DisplayName("게시글 저장 - 파일 확장자가 없을 경우")
    void savePost_fail_withoutFileExtension() throws Exception {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        MockMultipartFile file = new MockMultipartFile("files", "text", MediaType.TEXT_PLAIN_VALUE, "txt" .getBytes());

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Post title")
                    .formParam("content", "Post content")
                    .formParam("categoryId", category.getId())
                    .multiPart("files", "exeFile", file.getBytes(), MediaType.TEXT_PLAIN_VALUE)
                .when()
                    .post("/api/posts")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.INVALID_FILE_EXTENSION.name()))
                    .body("message", equalTo(ErrorCode.INVALID_FILE_EXTENSION.getMessage()));
    }

    @Test
    @DisplayName("게시글 상세 조회")
    @Transactional
    void getPost_success() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post = Post.of("Post title", "Post content", category);
        Attachment attachment = Attachment.of("text.txt", "saved_text.txt", MediaType.TEXT_PLAIN_VALUE, "txt", 1000L, "/upload");
        post.addAttachment(attachment);
        Post savedPost = postRepository.save(post);

        RestAssuredMockMvc
                .given()
                .when()
                    .get("/api/posts/{postId}", savedPost.getId())
                .then()
                    .statusCode(200)
                    .body("title", equalTo(post.getTitle()))
                    .body("content", equalTo(post.getContent()))
                    .body("category.id", equalTo(category.getId().intValue()))
                    .body("category.name", equalTo(category.getName()))
                    .body("attachments[0].id", equalTo(savedPost.getAttachments().get(0).getId().intValue()))
                    .body("attachments[0].name", equalTo(attachment.getOriginalFileName()))
                    .body("attachments[0].size", equalTo(attachment.getSize().intValue()));
    }

    @Test
    @DisplayName("게시글 상세 조회 - 게시글이 존재하지 않을 경우")
    void getPost_fail_postNotFound() {
        RestAssuredMockMvc
                .given()
                .when()
                    .get("/api/posts/{postId}", 9999L)
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.POST_NOT_FOUND.name()))
                    .body("message", equalTo(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("게시글 상세 조회 - 잘못된 ID 타입")
    void getPost_fail_invalidPostIdType() {
        RestAssuredMockMvc
                .given()
                .when()
                    .get("/api/posts/{postId}", "test")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.name()))
                    .body("message", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.getMessage()));
    }

    @Test
    @DisplayName("게시글 수정")
    @Transactional
    void updatePost() throws Exception {
        Category category1 = categoryRepository.save(Category.of("Category1", true, 1));
        Category category2 = categoryRepository.save(Category.of("Category2", true, 2));
        Post post = Post.of("Post title", "Post content", category1);
        Attachment attachment = Attachment.of("text.txt", "savedFile.txt", MediaType.TEXT_PLAIN_VALUE, "txt", 1000L, "/upload");
        post.addAttachment(attachment);
        Post savedPost = postRepository.save(post);

        MockMultipartFile newFile = new MockMultipartFile("files", "image.png", MediaType.IMAGE_PNG_VALUE, "image" .getBytes());

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Modify Post title")
                    .formParam("content", "Modify Post content")
                    .formParam("categoryId", category2.getId())
                    .formParam("deletedFileIds[0]", post.getAttachments().get(0).getId())
                    .multiPart("files", "image.png", newFile.getBytes(), MediaType.IMAGE_PNG_VALUE)
                .when()
                    .put("/api/posts/" + savedPost.getId())
                .then()
                    .statusCode(200)
                    .body(equalTo("ok"));

        Post modifiedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertThat(modifiedPost.getTitle()).isEqualTo("Modify Post title");
        assertThat(modifiedPost.getContent()).isEqualTo("Modify Post content");
        assertThat(modifiedPost.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(modifiedPost.getAttachments()).hasSize(1);
        assertThat(modifiedPost.getAttachments().get(0).getOriginalFileName()).isEqualTo("image.png");
    }

    @Test
    @DisplayName("게시글 수정 - 필수값 누락")
    void updatePost_fail_missingRequiredField() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post = postRepository.save(Post.of("Post title", "Post content", category));

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Modify Post title")
                    .formParam("content", "Modify Post content")
                .when()
                    .put("/api/posts/" + post.getId())
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.name()))
                    .body("message", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.getMessage()))
                    .body("errors.categoryId", equalTo("카테고리 ID를 입력해 주세요."));
    }

    @Test
    @DisplayName("게시글 수정 - 게시글이 존재하지 않을 경우")
    void updatePost_fail_postNotFound() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .formParam("title", "Modify Post title")
                    .formParam("content", "Modify Post content")
                    .formParam("categoryId", category.getId())
                .when()
                    .put("/api/posts/" + 9999L)
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.POST_NOT_FOUND.name()))
                    .body("message", equalTo(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost_success() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post = Post.of("Post title", "Post content", category);
        Attachment attachment = Attachment.of("text.txt", "savedFile.txt", MediaType.TEXT_PLAIN_VALUE, "txt", 1000L, "/upload");
        post.addAttachment(attachment);
        Post savedPost = postRepository.save(post);

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .delete("/api/posts/{postId}", savedPost.getId())
                .then()
                    .statusCode(200)
                    .body(equalTo("ok"));

        assertThat(postRepository.findById(savedPost.getId())).isEmpty();
        assertThat(postRepository.findById(savedPost.getAttachments().get(0).getId())).isEmpty();
    }

    @Test
    @DisplayName("게시글 삭제 - 게시글이 존재하지 않을 경우")
    void deletePost_fail_postNotFound() {
        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .delete("/api/posts/{postId}", 9999L)
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.POST_NOT_FOUND.name()))
                    .body("message", equalTo(ErrorCode.POST_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("게시글 개수 조회")
    void getPostCount_success() {
        Category category = categoryRepository.save(Category.of("Category", true, 1));
        Post post1 = postRepository.save(Post.of("Post title 1", "Post content 1", category));
        Post post2 = postRepository.save(Post.of("Post title 2", "Post content 2", category));

        RestAssuredMockMvc
                .given()
                    .param("categoryId", category.getId())
                .when()
                    .get("/api/posts/count")
                .then()
                    .statusCode(200)
                    .body(equalTo("2"));
    }

    @Test
    @DisplayName("게시글 개수 조회 실패 - 잘못된 ID 타입")
    void getPostCount_fail_invalidCategoryIdType() {
        RestAssuredMockMvc
                .given()
                    .param("categoryId", "test")
                .when()
                    .get("/api/posts/count")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.name()))
                    .body("message", equalTo(ErrorCode.PARAMETER_TYPE_MISMATCH.getMessage()));;
    }

}