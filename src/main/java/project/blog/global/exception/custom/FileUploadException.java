package project.blog.global.exception.custom;

import lombok.Getter;
import project.blog.global.config.common.ErrorCode;

@Getter
public class FileUploadException extends RuntimeException {

    private final ErrorCode errorCode;

    public FileUploadException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
