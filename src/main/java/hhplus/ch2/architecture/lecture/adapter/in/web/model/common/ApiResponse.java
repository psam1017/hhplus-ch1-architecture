package hhplus.ch2.architecture.lecture.adapter.in.web.model.common;

public class ApiResponse<T> {

    private final int code;
    private final T data;

    public ApiResponse(ApiCode apiCode, T data) {
        this.code = apiCode.getCode();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ApiCode.OK, data);
    }
}
