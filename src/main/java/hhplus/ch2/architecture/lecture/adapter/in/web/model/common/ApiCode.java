package hhplus.ch2.architecture.lecture.adapter.in.web.model.common;

public enum ApiCode {

    NO_SUCH_ELEMENT(1),
    POLICY_VIOLATION(2),
    OK(200),
    BAD_REQUEST(400),
    ;

    private final int code;

    ApiCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
