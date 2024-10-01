package hhplus.ch2.architecture.lecture.application.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hhplus.ch2.architecture.lecture.domain.Lecture;

import java.time.LocalDateTime;

public record LectureResponse(
        Long lectureId,
        String lectureTitle,
        String instructorName,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lectureDateTime,
        Long registeredCount,
        Long capacity
) {
    public LectureResponse(Long lectureId, String lectureTitle, String instructorName, LocalDateTime lectureDateTime, Long registeredCount) {
        this(lectureId, lectureTitle, instructorName, lectureDateTime, registeredCount, Lecture.CAPACITY);
    }
}
