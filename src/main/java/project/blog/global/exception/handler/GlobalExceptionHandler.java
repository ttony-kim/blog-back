package project.blog.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.blog.global.dto.ErrorResponse;
import project.blog.global.exception.custom.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e, "UNAUTHORIZED_USER"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotProvidedException.class)
    public ResponseEntity<ErrorResponse> handleFileNotProvidedException(FileNotProvidedException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e, "FILE_NOT_PROVIDED"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e, "FILE_UPLOAD_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e, "FILE_DOWNLOAD_ERROR"), HttpStatus.NOT_FOUND);
    }

    private ErrorResponse generateErrorResponse(RuntimeException e, String errorCode) {
        return new ErrorResponse(e.getMessage(), errorCode);
    }

}
