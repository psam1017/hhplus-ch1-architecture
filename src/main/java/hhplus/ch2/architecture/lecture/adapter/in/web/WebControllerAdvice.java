package hhplus.ch2.architecture.lecture.adapter.in.web;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;
import hhplus.ch2.architecture.lecture.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex, HttpServletRequest request) {

        log.error("[ApiException handle] URI = {}", request.getRequestURI());
        log.error("[reason] {}", ex.getErrorResponse().getMessage());

        return new ResponseEntity<>(ex.getErrorResponse(), OK);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException ex, HttpServletRequest request) {
        Optional<FieldError> optFieldError = ex.getFieldErrors().stream().findAny();
        String errorMessage = optFieldError.isPresent() ? optFieldError.get().getDefaultMessage() : "Bad Request";

        log.error("[BindException handle] URI = {}", request.getRequestURI());
        log.error("[reason] {}", errorMessage);

        ErrorResponse<String> body = new ErrorResponse<>(ApiCode.BAD_REQUEST, errorMessage, "Bad Request");
        return new ResponseEntity<>(body, BAD_REQUEST);
    }
}
