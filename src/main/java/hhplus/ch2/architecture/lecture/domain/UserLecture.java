package hhplus.ch2.architecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLecture {

    // TODO: 2024-10-01 ManyToOne ?
    private final Long userId;
    private final Long lectureId;
    private final LocalDateTime createdDateTime;

    @Builder
    protected UserLecture(Long userId, Long lectureId, LocalDateTime createdDateTime) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.createdDateTime = createdDateTime;
    }
}
