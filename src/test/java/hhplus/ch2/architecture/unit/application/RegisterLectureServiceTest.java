package hhplus.ch2.architecture.unit.application;

import hhplus.ch2.architecture.lecture.application.RegisterLectureService;
import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemInventoryRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemRepository;
import hhplus.ch2.architecture.lecture.application.port.out.UserLectureRepository;
import hhplus.ch2.architecture.lecture.application.port.out.UserRepository;
import hhplus.ch2.architecture.lecture.common.exception.AlreadyRegisteredLectureException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureItemException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureItemInventoryException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchUserException;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import hhplus.ch2.architecture.lecture.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterLectureServiceTest {

    @InjectMocks
    RegisterLectureService sut;

    @Mock
    UserRepository userRepository;

    @Mock
    LectureItemRepository lectureItemRepository;

    @Mock
    LectureItemInventoryRepository lectureItemInventoryRepository;

    @Mock
    UserLectureRepository userLectureRepository;

    @DisplayName("등록되지 않은 사용자는 강의를 신청할 수 없다.")
    @Test
    void registerLectureWithNotRegisteredUser() {
        // mock
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

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
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(lectureItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> sut.registerLecture(command))
                .isInstanceOf(NoSuchLectureItemException.class)
                .hasMessage("No such lecture item: 1");
    }

    @DisplayName("강의의 대기인원 정보를 찾지 못 하면 예외가 발생한다.")
    @Test
    void registerLectureWithNotRegisteredLectureItemInventory() {
        // mock
        User user = User.builder().build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureItemRepository.findById(anyLong())).thenReturn(Optional.of(LectureItem.builder().build()));
        when(lectureItemInventoryRepository.findByLectureItem(any())).thenReturn(Optional.empty());

        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> sut.registerLecture(command))
                .isInstanceOf(NoSuchLectureItemInventoryException.class)
                .hasMessage("No such lecture item inventory: 1");
    }

    @DisplayName("사용자는 이미 신청한 강의를 다시 신청할 수 없다.")
    @Test
    void registerLectureWithAlreadyRegisteredLecture() {
        // mock
        User user = User.builder().build();
        LectureItem lectureItem = LectureItem.builder().build();
        LectureItemInventory lectureItemInventory = LectureItemInventory.builder().leftSeat(1L).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureItemRepository.findById(anyLong())).thenReturn(Optional.of(lectureItem));
        when(lectureItemInventoryRepository.findByLectureItem(any())).thenReturn(Optional.of(lectureItemInventory));
        when(userLectureRepository.existsByUserLecture(any())).thenReturn(true);

        // given
        RegisterLectureCommand command = new RegisterLectureCommand(1L, 1L);

        // when
        // then
        assertThatThrownBy(() -> sut.registerLecture(command))
                .isInstanceOf(AlreadyRegisteredLectureException.class)
                .hasMessage("Lecture item %d is already registered".formatted(1L));
    }
}
