package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.InstructorEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LectureEntityTest {

    @DisplayName("Lecture 에서 LectureEntity 로 변환할 수 있다.")
    @Test
    void fromLecture() {
        // given
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        Lecture lecture = Lecture.builder()
                .id(1L)
                .title("강의 제목")
                .instructor(instructor)
                .build();

        // when
        LectureEntity entity = LectureEntity.fromDomain(lecture);

        // then
        assertThat(entity.getId()).isEqualTo(lecture.getId());
        assertThat(entity.getTitle()).isEqualTo(lecture.getTitle());
        assertThat(entity.getInstructorEntity().getId()).isEqualTo(lecture.getInstructor().getId());
    }

    @DisplayName("LectureEntity 에서 Lecture 로 변환할 수 있다.")
    @Test
    void toLecture() {
        // given
        InstructorEntity instructorEntity = InstructorEntity.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        LectureEntity entity = LectureEntity.builder()
                .id(1L)
                .title("강의 제목")
                .instructorEntity(instructorEntity)
                .build();

        // when
        Lecture lecture = entity.toDomain();

        // then
        assertThat(lecture.getId()).isEqualTo(entity.getId());
        assertThat(lecture.getTitle()).isEqualTo(entity.getTitle());
        assertThat(lecture.getInstructor().getId()).isEqualTo(entity.getInstructorEntity().getId());
    }
}
