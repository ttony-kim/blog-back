package project.blog.global.dto;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;


@Getter
public class ErrorResponse {

    private final String message;
    private final String errorCode;

    protected ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.errorCode = errorCode.name();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

}
