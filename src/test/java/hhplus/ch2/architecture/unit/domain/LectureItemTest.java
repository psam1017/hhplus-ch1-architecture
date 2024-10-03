package hhplus.ch2.architecture.unit.domain;

import hhplus.ch2.architecture.lecture.common.exception.LectureItemOutOfLeftSeatException;
import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LectureItemTest {

    @DisplayName("사용자를 최대 인원수 30명을 초과하여 등록할 수 없다.")
    @Test
    void registerUserUnderCapacity() {
        // given
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("instructor")
                .build();
        Lecture lecture = Lecture.builder()
                .id(1L)
                .title("title")
                .instructor(instructor)
                .build();
        LocalDateTime lectureDateTime = LocalDateTime.now();
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lecture(lecture)
                .lectureDateTime(lectureDateTime)
                .capacity(30L)
                .build();
        User user = User.builder()
                .id(1L)
                .name("user")
                .build();

        // when
        // then
        assertThatThrownBy(() -> lectureItem.registerUser(0, user))
                .isInstanceOf(LectureItemOutOfLeftSeatException.class)
                .hasMessage("Lecture item is out of left seat");
    }
}
