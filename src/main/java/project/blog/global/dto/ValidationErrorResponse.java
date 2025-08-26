package project.blog.global.dto;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;

import java.util.Map;

@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(Map<String, String> errors) {
        super(ErrorCode.REQUEST_VALIDATION_FAILED);
        this.errors = errors;
    }

}
