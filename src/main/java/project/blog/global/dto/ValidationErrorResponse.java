package project.blog.global.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(Map<String, String> errors) {
        super("유효하지 않은 입력입니다.", "REQUEST_VALIDATION_FAILED");
        this.errors = errors;
    }

}
