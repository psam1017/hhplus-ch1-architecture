package hhplus.ch2.architecture.unit.domain;

import hhplus.ch2.architecture.lecture.common.exception.LectureDateNotAllowedException;
import hhplus.ch2.architecture.lecture.common.exception.RegisteredCountOverCapacityException;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LectureTest {

    @DisplayName("특강은 토요일에만 등록할 수 있다.")
    @Test
    void registerUser() {
        // given
        LocalDateTime lectureDateTime = LocalDateTime.now().with(DayOfWeek.SUNDAY);

        // when
        // then
        assertThatThrownBy(() -> buildLecture("강의 제목", "강사 이름", lectureDateTime))
                .isInstanceOf(LectureDateNotAllowedException.class)
                .hasMessage("Lecture is only allowed on [SATURDAY]");
    }

    @DisplayName("사용자를 최대 인원수 30명을 초과하여 등록할 수 없다.")
    @Test
    void registerUserUnderCapacity() {
        // given
        Lecture lecture = buildLecture("강의 제목", "강사 이름", LocalDateTime.now().with(DayOfWeek.SATURDAY));
        User user = User.builder()
                .id(1L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> lecture.registerUser(30, user))
                .isInstanceOf(RegisteredCountOverCapacityException.class)
                .hasMessage("Lecture is full: 30");
    }

    private Lecture buildLecture(String title, String instructorName, LocalDateTime lectureDateTime) {
        return Lecture.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }
}
