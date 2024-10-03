package hhplus.ch2.architecture.lecture.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLecture {

    private final User user;
    private final LectureItem lectureItem;
    private final LocalDateTime createdDateTime;

    @Builder
    protected UserLecture(User user, LectureItem lectureItem, LocalDateTime createdDateTime) {
        this.user = user;
        this.lectureItem = lectureItem;
        this.createdDateTime = createdDateTime;
    }
}
