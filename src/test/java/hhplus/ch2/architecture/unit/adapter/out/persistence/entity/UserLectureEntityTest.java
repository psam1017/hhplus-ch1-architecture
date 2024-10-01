package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.User;
import hhplus.ch2.architecture.lecture.domain.UserLecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserLectureEntityTest {

    @DisplayName("UserLecture 로부터 UserLectureEntity 를 생성할 수 있다.")
    @Test
    void fromUserLecture() {
        // given
        User user = User.builder().id(1L).build();
        Lecture lecture = buildLecture("강의 제목", "강사 이름", LocalDateTime.now().with(DayOfWeek.SATURDAY));

        UserLecture userLecture = UserLecture.builder()
                .user(user)
                .lecture(lecture)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLectureEntity entity = UserLectureEntity.fromDomain(userLecture);

        // then
        assertThat(entity.getUser().getId()).isEqualTo(userLecture.getUser().getId());
        assertThat(entity.getLecture().getId()).isEqualTo(userLecture.getLecture().getId());
        assertThat(entity.getLecture().getTitle()).isEqualTo(userLecture.getLecture().getTitle());
        assertThat(entity.getLecture().getInstructorName()).isEqualTo(userLecture.getLecture().getInstructorName());
        assertThat(entity.getLecture().getLectureDateTime()).isEqualTo(userLecture.getLecture().getLectureDateTime());
        assertThat(entity.getCreatedDateTime()).isEqualTo(userLecture.getCreatedDateTime());
    }

    @DisplayName("UserLectureEntity 에서 UserLecture 로 변환할 수 있다.")
    @Test
    void toUserLecture() {
        // given
        UserLectureEntity entity = UserLectureEntity.builder()
                .user(UserEntity.builder().id(1L).build())
                .lecture(buildLectureEntity(1L, "강의 제목", "강사 이름", LocalDateTime.now().with(DayOfWeek.SATURDAY)))
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLecture userLecture = entity.toDomain();

        // then
        assertThat(userLecture.getUser().getId()).isEqualTo(entity.getUser().getId());
        assertThat(userLecture.getLecture().getId()).isEqualTo(entity.getLecture().getId());
        assertThat(userLecture.getLecture().getTitle()).isEqualTo(entity.getLecture().getTitle());
        assertThat(userLecture.getLecture().getInstructorName()).isEqualTo(entity.getLecture().getInstructorName());
        assertThat(userLecture.getLecture().getLectureDateTime()).isEqualTo(entity.getLecture().getLectureDateTime());
        assertThat(userLecture.getCreatedDateTime()).isEqualTo(entity.getCreatedDateTime());
    }

    private Lecture buildLecture(String title, String instructorName, LocalDateTime lectureDateTime) {
        return Lecture.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }

    private LectureEntity buildLectureEntity(Long id, String title, String instructorName, LocalDateTime lectureDateTime) {
        return LectureEntity.builder()
                .id(id)
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }
}
