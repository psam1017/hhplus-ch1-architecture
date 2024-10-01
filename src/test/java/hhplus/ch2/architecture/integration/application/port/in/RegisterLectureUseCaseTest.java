package hhplus.ch2.architecture.integration.application.port.in;

import hhplus.ch2.architecture.integration.application.SpringBootTestEnvironment;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureId;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import hhplus.ch2.architecture.lecture.application.RegisterLectureService;
import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterLectureUseCaseTest extends SpringBootTestEnvironment {

    @Autowired
    RegisterLectureService sut;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;

    @DisplayName("사용자가 강의를 신청할 수 있다.")
    @Test
    void registerLecture() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        LectureEntity lecture = buildLectureEntity("강의1", "강사1", thisSaturday);
        lectureJpaRepository.save(lecture);

        UserEntity user = UserEntity.empty();
        userJpaRepository.save(user);

        RegisterLectureCommand command = new RegisterLectureCommand(user.getId(), lecture.getId());

        // when
        LectureRegistrationResult result = sut.registerLecture(command);

        // then
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.lectureId()).isEqualTo(lecture.getId());

        Optional<UserLectureEntity> optUserLecture = userLectureJpaRepository.findById(buildUserLectureId(user.getId(), lecture.getId()));
        assertThat(optUserLecture).isPresent();
    }

    private LectureEntity buildLectureEntity(String title, String instructorName, LocalDateTime lectureDateTime) {
        return LectureEntity.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }

    private UserLectureId buildUserLectureId(Long userId, Long lectureId) {
        return UserLectureId.builder()
                .userId(userId)
                .lectureId(lectureId)
                .build();
    }
}
