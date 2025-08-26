package project.blog.global.exception.custom;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;

@Getter
public class FileNotProvidedException extends RuntimeException {

    private final ErrorCode errorCode;

    public FileNotProvidedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
