package hhplus.ch2.architecture.integration.application.port.in;

import hhplus.ch2.architecture.integration.application.SpringBootTestEnvironment;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.FindAvailableLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FindAvailableLecturesUseCaseTest extends SpringBootTestEnvironment {

    @Autowired
    FindAvailableLecturesUseCase sut;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @DisplayName("수강할 수 있는 모든 강의를 찾을 수 있다.")
    @Test
    void findAvailableLectures() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        LectureEntity lectureEntity1 = buildLectureEntity("강의1", "강사1", thisSaturday);
        LectureEntity lectureEntity2 = buildLectureEntity("강의2", "강사2", thisSaturday.plusWeeks(1));
        lectureJpaRepository.saveAll(List.of(lectureEntity1, lectureEntity2));

        FindAvailableLectureCommand command = new FindAvailableLectureCommand(null);

        // when
        List<LectureResponse> lectures = sut.findAvailableLectures(command);

        // then
        assertThat(lectures).hasSize(2)
                .extracting(l -> tuple(l.lectureId(), l.lectureTitle()))
                .containsExactlyInAnyOrder(
                        tuple(lectureEntity1.getId(), "강의1"),
                        tuple(lectureEntity2.getId(), "강의2")
                );
    }

    @DisplayName("특정 일자의 강의만 조회할 수 있다.")
    @Test
    void findAvailableLecturesWithLectureDate() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        LectureEntity thisWeekLecture = buildLectureEntity("강의1", "강사1", thisSaturday);
        LectureEntity nextWeekLecture = buildLectureEntity("강의2", "강사2", thisSaturday.plusWeeks(1));
        lectureJpaRepository.saveAll(List.of(thisWeekLecture, nextWeekLecture));

        FindAvailableLectureCommand command = new FindAvailableLectureCommand(thisSaturday.toLocalDate());

        // when
        List<LectureResponse> lectures = sut.findAvailableLectures(command);

        // then
        assertThat(lectures).hasSize(1)
                .extracting(l -> tuple(l.lectureId(), l.lectureTitle()))
                .containsExactly(tuple(thisWeekLecture.getId(), "강의1"));
    }

    private LectureEntity buildLectureEntity(String title, String instructorName, LocalDateTime lectureDateTime) {
        return LectureEntity.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }
}
