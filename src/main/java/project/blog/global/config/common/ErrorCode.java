package project.blog.global.config.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 카테고리, 게시글, 첨부파일
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다."),
    ATTACHMENT_NOT_FOUND("첨부파일을 찾을 수 없습니다."),
    //파일
    FILE_NOT_PROVIDED("파일이 제공되지 않았습니다."),
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다."),
    FILE_DIRECTORY_CREATION_FAILED("파일 디렉터리 생성에 실패했습니다."),
    INVALID_FILE_EXTENSION("잘못된 파일 확장자입니다."),
    NOT_ALLOWED_FILE_EXTENSION("허용되지 않은 파일 확장자입니다."),
    FILE_NOT_FOUND_OR_NOT_READABLE("파일을 찾을 수 없거나 읽을 수 없습니다."),
    INVALID_FILE_PATH("유효하지 않은 파일 경로입니다."),
    // 사용자, 인증
    INVALID_EMAIL_OR_PASSWORD("이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    MALFORMED_TOKEN("잘못된 형식의 토큰입니다."),
    NO_TOKEN_PROVIDED("토큰이 제공되지 않았습니다."),
    // 요청
    REQUEST_VALIDATION_FAILED("잘못된 요청 값이 존재합니다."),
    INVALID_REQUEST_BODY("요청 JSON 형식이 잘못되었거나 데이터 타입이 올바르지 않습니다."),
    PARAMETER_TYPE_MISMATCH("요청 파라미터 타입이 올바르지 않습니다."),
    // 서버
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
