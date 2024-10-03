package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

public class AlreadyRegisteredLectureException extends ApiException {

    public AlreadyRegisteredLectureException(Long lectureItemId) {
        super(ErrorResponse.error(ApiCode.POLICY_VIOLATION, null, "Lecture item %d is already registered".formatted(lectureItemId)));
    }
}
