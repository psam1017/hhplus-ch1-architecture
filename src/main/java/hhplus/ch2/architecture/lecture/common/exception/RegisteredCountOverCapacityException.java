package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

public class RegisteredCountOverCapacityException extends ApiException {

    public RegisteredCountOverCapacityException(long capacity) {
        super(ErrorResponse.error(ApiCode.POLICY_VIOLATION, null, "Lecture is full: " + capacity));
    }
}
