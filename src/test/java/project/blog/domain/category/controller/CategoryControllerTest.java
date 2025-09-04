package project.blog.domain.category.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import project.blog.domain.category.dto.CategoryRequestDto;
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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        postRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Category 목록 조회")
    void getCategories_success() {
        Category category1 = categoryRepository.save(Category.of("Category1", true, 1));
        Category category2 = categoryRepository.save(Category.of("Category2", false, 2));

        RestAssuredMockMvc
                .given()
                .when()
                    .get("/api/categories")
                .then()
                    .statusCode(200)
                    .body("size()", equalTo(1))
                    .body("[0].name", equalTo(category1.getName()));
    }

    @Test
    @DisplayName("Category 목록 상세 조회")
    void getCategoryDetails_success() {
        Category category1 = categoryRepository.save(Category.of("Category1", true, 1));
        Category category2 = categoryRepository.save(Category.of("Category2", false, 2));

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .get("/api/categories/detail")
                .then()
                    .statusCode(200)
                    .body("size()", equalTo(2))
                    .body("[1].name", equalTo(category2.getName()))
                    .body("[1].enabled", equalTo(category2.getEnabled()))
                    .body("[1].displayOrder", equalTo(category2.getDisplayOrder()));
    }

    @Test
    @DisplayName("Category 목록 상세 조회 - 토큰 누락")
    void getCategoryDetails_fail_missingToken() {
        Category category1 = categoryRepository.save(Category.of("Category1", true, 1));
        Category category2 = categoryRepository.save(Category.of("Category2", false, 2));

        RestAssuredMockMvc
                .given()
                .when()
                    .get("/api/categories/detail")
                .then()
                    .statusCode(401)
                    .body("errorCode", equalTo(ErrorCode.NO_TOKEN_PROVIDED.name()))
                    .body("message", equalTo(ErrorCode.NO_TOKEN_PROVIDED.getMessage()));
    }

    @Test
    @DisplayName("Category 목록 저장")
    void saveCategories_success() {
        Category category1 = categoryRepository.save(Category.of("Category1", true, 1));
        Category category2 = categoryRepository.save(Category.of("Category2", false, 2));
        Post post = postRepository.save(Post.of("Post title", "Post Content", category2));

        CategoryRequestDto.CategoryDto newCategory = new CategoryRequestDto.CategoryDto();
        newCategory.setName("New Category");
        newCategory.setEnabled(true);
        newCategory.setDisplayOrder(1);

        CategoryRequestDto.CategoryDto modifyCategory = new CategoryRequestDto.CategoryDto();
        modifyCategory.setId(category1.getId());
        modifyCategory.setName("Modify Category");
        modifyCategory.setEnabled(false);
        modifyCategory.setDisplayOrder(2);

        CategoryRequestDto requestDto = new CategoryRequestDto(List.of(newCategory, modifyCategory));

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(requestDto)
                .when()
                    .post("/api/categories")
                .then()
                    .statusCode(200)
                    .body(equalTo("ok"));

        List<Category> allCategories = categoryRepository.findAll();
        assertThat(allCategories).hasSize(2);

        Category newCategorySaved = allCategories.stream()
                .filter(c -> c.getName().equals(newCategory.getName()))
                .findFirst()
                .orElseThrow();
        assertThat(newCategorySaved.getEnabled()).isEqualTo(newCategory.getEnabled());
        assertThat(newCategorySaved.getDisplayOrder()).isEqualTo(newCategory.getDisplayOrder());

        Category modifyCategorySaved = allCategories.stream()
                .filter(c -> c.getId().equals(category1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(modifyCategorySaved.getName()).isEqualTo(modifyCategorySaved.getName());
        assertThat(modifyCategorySaved.getEnabled()).isEqualTo(modifyCategorySaved.getEnabled());
        assertThat(modifyCategorySaved.getDisplayOrder()).isEqualTo(modifyCategorySaved.getDisplayOrder());

        List<Post> allPosts = postRepository.findAll();
        assertThat(allPosts).hasSize(0);
    }

    @Test
    @DisplayName("Category 목록 저장 - 필수값 누락")
    void saveCategories_fail_missingRequiredField() {
        CategoryRequestDto.CategoryDto newCategory = new CategoryRequestDto.CategoryDto();
        newCategory.setEnabled(true);
        newCategory.setDisplayOrder(1);

        CategoryRequestDto requestDto = new CategoryRequestDto(List.of(newCategory));

        RestAssuredMockMvc
                .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(requestDto)
                .when()
                    .post("/api/categories")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.name()))
                    .body("message", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.getMessage()))
                    .body("errors['categories[0].name']", equalTo("카테고리 명을 입력해 주세요."));
    }

}