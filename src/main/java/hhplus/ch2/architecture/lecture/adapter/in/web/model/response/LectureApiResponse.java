package hhplus.ch2.architecture.lecture.adapter.in.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record LectureApiResponse(
        Long lectureId,
        String lectureTitle,
        String instructorName,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lectureDateTime,
        Long leftSeat,
        Long capacity
) {
}
