package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

public class NoSuchUserException extends ApiException {

    public NoSuchUserException(Long userId) {
        super(ErrorResponse.error(ApiCode.NO_SUCH_ELEMENT, null, "No such user: " + userId));
    }
}
