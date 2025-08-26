package project.blog.global.exception.custom;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
