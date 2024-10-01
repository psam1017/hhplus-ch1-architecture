package hhplus.ch2.architecture.lecture.adapter.in.web.model.request;

import jakarta.validation.constraints.NotNull;

public record LectureRegistrationForm(@NotNull Long userId) {

}
