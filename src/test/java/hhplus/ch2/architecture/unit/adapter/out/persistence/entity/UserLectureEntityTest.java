package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.domain.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserLectureEntityTest {

    @DisplayName("UserLecture 로부터 UserLectureEntity 를 생성할 수 있다.")
    @Test
    void fromUserLecture() {
        // given
        User user = User.builder()
                .id(1L)
                .name("사용자 이름")
                .build();
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        Lecture lecture = Lecture.builder()
                .id(1L)
                .title("강의 제목")
                .instructor(instructor)
                .build();
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lecture(lecture)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .build();

        UserLecture userLecture = UserLecture.builder()
                .user(user)
                .lectureItem(lectureItem)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLectureEntity entity = UserLectureEntity.fromDomain(userLecture);

        // then
        assertThat(entity.getUserEntity().getId()).isEqualTo(userLecture.getUser().getId());
        assertThat(entity.getLectureItemEntity().getId()).isEqualTo(userLecture.getLectureItem().getId());
        assertThat(entity.getCreatedDateTime()).isEqualTo(userLecture.getCreatedDateTime());
    }

    @DisplayName("UserLectureEntity 에서 UserLecture 로 변환할 수 있다.")
    @Test
    void toUserLecture() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("사용자 이름")
                .build();
        InstructorEntity instructorEntity = InstructorEntity.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        LectureEntity lectureEntity = LectureEntity.builder()
                .id(1L)
                .title("강의 제목")
                .instructorEntity(instructorEntity)
                .build();
        LectureItemEntity lectureItemEntity = LectureItemEntity.builder()
                .id(1L)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .lectureEntity(lectureEntity)
                .build();

        UserLectureEntity entity = UserLectureEntity.builder()
                .userEntity(userEntity)
                .lectureItemEntity(lectureItemEntity)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLecture userLecture = entity.toDomain();

        // then
        assertThat(userLecture.getUser().getId()).isEqualTo(entity.getUserEntity().getId());
        assertThat(userLecture.getLectureItem().getId()).isEqualTo(entity.getLectureItemEntity().getId());
        assertThat(userLecture.getCreatedDateTime()).isEqualTo(entity.getCreatedDateTime());
    }
}
