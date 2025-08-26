package project.blog.global.exception.custom;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
