package hhplus.ch2.architecture.lecture.common.exception;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiCode;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ErrorResponse;

import java.time.DayOfWeek;
import java.util.Arrays;

public class LectureDateNotAllowedException extends ApiException {

    public LectureDateNotAllowedException(DayOfWeek[] ALLOWED_DAYS) {
        super(ErrorResponse.error(ApiCode.POLICY_VIOLATION, null, "Lecture is only allowed on " + Arrays.toString(ALLOWED_DAYS)));
    }
}
