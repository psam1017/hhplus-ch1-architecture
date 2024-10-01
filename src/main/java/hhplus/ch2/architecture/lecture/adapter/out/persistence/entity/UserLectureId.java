package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class UserLectureId implements Serializable {

    private Long user;
    private Long lecture;

    protected UserLectureId() {
    }

    @Builder
    protected UserLectureId(Long userId, Long lectureId) {
        this.user = userId;
        this.lecture = lectureId;
    }
}
