package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

public class ApiException extends RuntimeException {

    private final ErrorResponse<Object> errorResponse;

    public ApiException(ErrorResponse<Object> errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse<Object> getErrorResponse() {
        return errorResponse;
    }
}
