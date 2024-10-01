package hhplus.ch2.architecture.lecture.adapter.in.web.model.common;

public class ErrorResponse<T> extends ApiResponse<T> {

    private final String message;

    public ErrorResponse(ApiCode apiCode, T data, String message) {
        super(apiCode, data);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static <T> ErrorResponse<T> error(ApiCode apiCode, T data, String message) {
        return new ErrorResponse<>(apiCode, data, message);
    }
}
