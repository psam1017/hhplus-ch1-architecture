package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LectureEntityTest {

    @DisplayName("LectureEntity 에서 Lecture 로 변환할 수 있다.")
    @Test
    void toLecture() {
        // given
        LectureEntity entity = LectureEntity.builder()
                .id(1L)
                .title("강의 제목")
                .instructorName("강사 이름")
                .lectureDateTime(LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0))
                .build();

        // when
        Lecture lecture = entity.toDomain();

        // then
        assertThat(lecture.getId()).isEqualTo(entity.getId());
        assertThat(lecture.getTitle()).isEqualTo(entity.getTitle());
        assertThat(lecture.getInstructorName()).isEqualTo(entity.getInstructorName());
        assertThat(lecture.getLectureDateTime()).isEqualTo(entity.getLectureDateTime());
    }
}
