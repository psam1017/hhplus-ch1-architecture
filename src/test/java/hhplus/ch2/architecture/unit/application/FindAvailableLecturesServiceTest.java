package hhplus.ch2.architecture.unit.application;

import hhplus.ch2.architecture.lecture.application.FindAvailableLecturesService;
import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAvailableLecturesServiceTest {

    @InjectMocks
    FindAvailableLecturesService sut;

    @Mock
    LecturePort lecturePort;

    @DisplayName("모든 날짜의 강의 목록을 조회할 수 있다.")
    @Test
    void findAvailableLecturesWithoutLectureDate() {
        // mock
        when(
                lecturePort.findAllAvailableLectures(anyLong())
        ).thenReturn(
                List.of(
                        new LectureResponse(1L, "강의", "강사", LocalDateTime.now(), 0L)
                )
        );

        // given
        FindAvailableLectureCommand command = new FindAvailableLectureCommand(null);

        // when
        List<LectureResponse> lectureResponses = sut.findAvailableLectures(command);

        // then
        assertThat(lectureResponses).hasSize(1)
                .extracting(LectureResponse::lectureId)
                .containsExactly(1L);
    }

    @DisplayName("특정 날짜의 강의 목록을 조회할 수 있다.")
    @Test
    void findAvailableLecturesWithLectureDate() {
        // mock
        when(
                lecturePort.findAllAvailableLectures(any(), anyLong())
        ).thenReturn(
                List.of(
                        new LectureResponse(1L, "강의", "강사", LocalDateTime.now(), 0L)
                )
        );

        // given
        FindAvailableLectureCommand command = new FindAvailableLectureCommand(LocalDate.now());

        // when
        List<LectureResponse> lectureResponses = sut.findAvailableLectures(command);

        // then
        assertThat(lectureResponses).hasSize(1)
                .extracting(LectureResponse::lectureId)
                .containsExactly(1L);
    }
}
