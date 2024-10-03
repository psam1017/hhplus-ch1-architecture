package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class UserLectureEntityId implements Serializable {

    private Long userEntity;
    private Long lectureItemEntity;

    protected UserLectureEntityId() {
    }

    @Builder
    protected UserLectureEntityId(Long userEntityId, Long lectureItemEntityId) {
        this.userEntity = userEntityId;
        this.lectureItemEntity = lectureItemEntityId;
    }
}
