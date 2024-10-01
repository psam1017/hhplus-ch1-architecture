package hhplus.ch2.architecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLecture {

    // TODO: 2024-10-01 ManyToOne ?
    private final User user;
    private final Lecture lecture;
    private final LocalDateTime createdDateTime;

    @Builder
    protected UserLecture(User user, Lecture lecture, LocalDateTime createdDateTime) {
        this.user = user;
        this.lecture = lecture;
        this.createdDateTime = createdDateTime;
    }
}
