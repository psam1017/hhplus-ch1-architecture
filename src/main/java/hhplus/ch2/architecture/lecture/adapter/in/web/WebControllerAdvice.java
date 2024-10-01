package hhplus.ch2.architecture.lecture.adapter.in.web;

import hhplus.ch2.architecture.lecture.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
