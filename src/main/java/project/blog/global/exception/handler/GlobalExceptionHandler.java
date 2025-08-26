package project.blog.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import project.blog.global.dto.ErrorResponse;
import project.blog.global.dto.ValidationErrorResponse;
import project.blog.global.exception.custom.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), "UNAUTHORIZED_USER"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotProvidedException.class)
    public ResponseEntity<ErrorResponse> handleFileNotProvidedException(FileNotProvidedException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), "FILE_NOT_PROVIDED"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), "FILE_UPLOAD_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), "FILE_DOWNLOAD_ERROR"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(Exception e) {
        e.printStackTrace();

        BindingResult bindingResult = null;
        if (e instanceof BindException be) {
            bindingResult = be.getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException manv) {
            bindingResult = manv.getBindingResult();
        }

        Map<String, String> errors = new HashMap<>();

        if (bindingResult != null) {
            for(FieldError error : bindingResult.getFieldErrors()) {
                if ("typeMismatch".equals(error.getCode())) {
                    errors.merge(error.getField(), "데이터 타입이 올바르지 않습니다.", (oldVal, newVal) -> oldVal + ", " + newVal);
                } else {
                    errors.merge(error.getField(), error.getDefaultMessage(), (oldVal, newVal) -> oldVal + ", " + newVal);
                }
            }
        }

        return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse("JSON 형식이 잘못되었거나 데이터 타입이 올바르지 않습니다.", "INVALID_REQUEST_BODY"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();

        return new ResponseEntity<>(generateErrorResponse( "잘못된 요청 파라미터입니다.", "PARAMETER_TYPE_MISMATCH"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e. printStackTrace();

        return new ResponseEntity<>(generateErrorResponse("서버 내부 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse generateErrorResponse(String message, String errorCode) {
        return new ErrorResponse(message, errorCode);
    }

}
