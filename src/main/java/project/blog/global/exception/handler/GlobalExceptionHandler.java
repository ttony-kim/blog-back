package project.blog.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.blog.global.dto.ErrorResponse;
import project.blog.global.exception.custom.BadRequestException;
import project.blog.global.exception.custom.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "UNAUTHORIZED_USER");

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "BAD_REQUEST");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
