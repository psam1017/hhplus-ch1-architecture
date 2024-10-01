package hhplus.ch2.architecture.unit.application;

import hhplus.ch2.architecture.lecture.application.RegisterLectureService;
import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.application.port.out.UserLecturePort;
import hhplus.ch2.architecture.lecture.application.port.out.UserPort;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchUserException;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterLectureServiceTest {

    @InjectMocks
    RegisterLectureService sut;

    @Mock
    UserPort userPort;

    @Mock
    LecturePort lecturePort;

    @Mock
    UserLecturePort userLecturePort;

    @DisplayName("사용자가 강의를 신청할 수 있다.")
    @Test
    void registerLecture() {
        // mock
        when(userPort.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(lecturePort.findById(anyLong())).thenReturn(Optional.of(Lecture.builder()
                .lectureDateTime(LocalDateTime.now().with(DayOfWeek.SATURDAY))
                .build()));
        when(userLecturePort.countByLecture(any())).thenReturn(0L);

        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        LectureRegistrationResult result = sut.registerLecture(command);

        // then
        assertThat(result.lectureId()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(1L);
    }

    @DisplayName("등록되지 않은 사용자는 강의를 신청할 수 없다.")
    @Test
    void registerLectureWithNotRegisteredUser() {
        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> sut.registerLecture(command))
                .isInstanceOf(NoSuchUserException.class)
                .hasMessage("No such user: 1");
    }

    @DisplayName("등록되지 않은 강의는 사용자가 신청할 수 없다.")
    @Test
    void registerLectureWithNotRegisteredLecture() {
        // mock
        when(userPort.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(lecturePort.findById(anyLong())).thenReturn(Optional.empty());

        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> sut.registerLecture(command))
                .isInstanceOf(NoSuchLectureException.class)
                .hasMessage("No such lecture: 1");
    }
}
