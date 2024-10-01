package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.domain.UserLecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserLectureEntityTest {

    @DisplayName("UserLecture 로부터 UserLectureEntity 를 생성할 수 있다.")
    @Test
    void fromUserLecture() {
        // given
        UserLecture userLecture = UserLecture.builder()
                .userId(1L)
                .lectureId(1L)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLectureEntity entity = UserLectureEntity.fromDomain(userLecture);

        // then
        assertThat(entity.getUserId()).isEqualTo(userLecture.getUserId());
        assertThat(entity.getLectureId()).isEqualTo(userLecture.getLectureId());
        assertThat(entity.getCreatedDateTime()).isEqualTo(userLecture.getCreatedDateTime());
    }

    @DisplayName("UserLectureEntity 에서 UserLecture 로 변환할 수 있다.")
    @Test
    void toUserLecture() {
        // given
        UserLectureEntity entity = UserLectureEntity.builder()
                .userId(1L)
                .lectureId(1L)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLecture userLecture = entity.toDomain();

        // then
        assertThat(userLecture.getUserId()).isEqualTo(entity.getUserId());
        assertThat(userLecture.getLectureId()).isEqualTo(entity.getLectureId());
        assertThat(userLecture.getCreatedDateTime()).isEqualTo(entity.getCreatedDateTime());
    }
}
