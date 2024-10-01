package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class UserLectureId implements Serializable {

    private Long userId;
    private Long lectureId;

    protected UserLectureId() {
    }

    @Builder
    protected UserLectureId(Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }
}
