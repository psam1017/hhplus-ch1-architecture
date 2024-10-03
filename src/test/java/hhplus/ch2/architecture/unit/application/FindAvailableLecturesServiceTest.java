package hhplus.ch2.architecture.unit.application;

import hhplus.ch2.architecture.lecture.application.FindAvailableLecturesService;
import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemInventoryRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemRepository;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAvailableLecturesServiceTest {

    @InjectMocks
    FindAvailableLecturesService sut;

    @Mock
    LectureItemRepository lectureItemRepository;

    @Mock
    LectureItemInventoryRepository lectureItemInventoryRepository;

    @DisplayName("모든 날짜의 신청 가능한 강의를 조회할 수 있다.")
    @Test
    void findAvailableLecturesAllDate() {
        // mock
        List<LectureItem> expectedResponses = List.of(LectureItem.builder()
                .id(1L)
                .lecture(buildLecture())
                .lectureDateTime(LocalDateTime.now())
                .capacity(10L)
                .build());

        when(lectureItemRepository.findAllAvailableLectureItems())
                .thenReturn(expectedResponses);
        when(lectureItemInventoryRepository.findLeftSeatByLectureItems(List.of(1L)))
                .thenReturn(Map.of(1L, 0L));
        // given

        FindAvailableLectureCommand command = new FindAvailableLectureCommand(null);

        // when
        List<LectureResponse> responses = sut.findAvailableLectureItems(command);

        // then
        LectureItem expected = expectedResponses.get(0);
        assertThat(responses).hasSize(1)
                .extracting(lecture -> tuple(lecture.lectureId(), lecture.lectureTitle(), lecture.instructorName(), lecture.lectureDateTime(), lecture.leftSeat(), lecture.capacity()))
                .contains(
                        tuple(
                                expected.getId(),
                                expected.getLecture().getTitle(),
                                expected.getLecture().getInstructor().getName(),
                                expected.getLectureDateTime(),
                                0L,
                                expected.getCapacity()
                        )
                );
    }

    @DisplayName("특정 날짜의 신청 가능한 강의를 조회할 수 있다.")
    @Test
    void findAvailableLecturesSpecificDate() {
        // mock
        LocalDate searchDate = LocalDate.now();
        List<LectureItem> expectedResponses = List.of(LectureItem.builder()
                .id(1L)
                .lecture(buildLecture())
                .lectureDateTime(LocalDateTime.now())
                .capacity(10L)
                .build());

        when(lectureItemRepository.findAllAvailableLectureItems(eq(searchDate)))
                .thenReturn(expectedResponses);
        when(lectureItemInventoryRepository.findLeftSeatByLectureItems(List.of(1L)))
                .thenReturn(Map.of(1L, 0L));

        // given
        FindAvailableLectureCommand command = new FindAvailableLectureCommand(searchDate);

        // when
        List<LectureResponse> responses = sut.findAvailableLectureItems(command);

        // then
        LectureItem expected = expectedResponses.get(0);
        assertThat(responses).hasSize(1)
                .extracting(lecture -> tuple(lecture.lectureId(), lecture.lectureTitle(), lecture.instructorName(), lecture.lectureDateTime(), lecture.leftSeat(), lecture.capacity()))
                .contains(
                        tuple(
                                expected.getId(),
                                expected.getLecture().getTitle(),
                                expected.getLecture().getInstructor().getName(),
                                expected.getLectureDateTime(),
                                0L,
                                expected.getCapacity()
                        )
                );
    }

    private static Lecture buildLecture() {
        return Lecture.builder()
                .id(1L)
                .title("강의 제목")
                .instructor(buildInstructor())
                .build();
    }

    private static Instructor buildInstructor() {
        return Instructor.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
    }
}
