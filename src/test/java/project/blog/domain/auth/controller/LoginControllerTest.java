package project.blog.domain.auth.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import project.blog.domain.auth.dto.LoginDto;
import project.blog.domain.member.entity.Member;
import project.blog.domain.member.repository.MemberRepository;
import project.blog.global.config.common.BCryptEncryptor;
import project.blog.global.config.common.ErrorCode;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptEncryptor bCryptEncryptor;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 및 토큰 발급")
    void login_success() {
        Member member = Member.of("test", bCryptEncryptor.encryptPassword("test"),"test");
        memberRepository.save(member);

        LoginDto requestDto = new LoginDto("test", "test");

        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(requestDto)
                .when()
                    .post("/api/login")
                .then()
                    .statusCode(200);
    }

    @Test
    @DisplayName("로그인 실패")
    void login_fail() {
        Member member = Member.of("test", bCryptEncryptor.encryptPassword("test"),"test");
        memberRepository.save(member);

        LoginDto requestDto = new LoginDto("test", "test123");

        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(requestDto)
                .when()
                    .post("/api/login")
                .then()
                    .statusCode(401)
                    .body("errorCode", equalTo(ErrorCode.INVALID_EMAIL_OR_PASSWORD.name()))
                    .body("message", equalTo(ErrorCode.INVALID_EMAIL_OR_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("로그인 - 필수값 누락")
    void login_fail_missingRequiredField() {
        LoginDto requestDto = new LoginDto("test", null);

        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(requestDto)
                .when()
                    .post("/api/login")
                .then()
                    .statusCode(400)
                    .body("errorCode", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.name()))
                    .body("message", equalTo(ErrorCode.REQUEST_VALIDATION_FAILED.getMessage()))
                    .body("errors.password", equalTo("Password를 입력해 주세요."));
    }

}