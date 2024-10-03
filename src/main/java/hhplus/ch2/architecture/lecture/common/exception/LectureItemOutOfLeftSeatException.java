package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

public class LectureItemOutOfLeftSeatException extends ApiException {

    public LectureItemOutOfLeftSeatException() {
        super(ErrorResponse.error(ApiCode.POLICY_VIOLATION, null, "Lecture item is out of left seat"));
    }
}
