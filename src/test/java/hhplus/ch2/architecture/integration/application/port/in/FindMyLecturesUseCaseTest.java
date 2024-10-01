package hhplus.ch2.architecture.integration.application.port.in;

import hhplus.ch2.architecture.integration.application.SpringBootTestEnvironment;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import hhplus.ch2.architecture.lecture.application.port.in.FindMyLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FindMyLecturesUseCaseTest extends SpringBootTestEnvironment {

    @Autowired
    FindMyLecturesUseCase sut;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;

    @DisplayName("내가 신청한 강의를 찾을 수 있다.")
    @Test
    void findMyLectures() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        UserEntity userEntity = userJpaRepository.save(UserEntity.empty());

        LectureEntity myLecture = buildLectureEntity("강의1", "강사1", thisSaturday);
        LectureEntity anotherLecture = buildLectureEntity("강의2", "강사2", thisSaturday);
        lectureJpaRepository.saveAll(List.of(myLecture, anotherLecture));

        UserLectureEntity userLectureEntity = buildUserLectureEntity(userEntity, myLecture);
        userLectureJpaRepository.save(userLectureEntity);

        // when
        List<LectureResponse> lectures = sut.findMyLectures(userEntity.getId());

        // then
        assertThat(lectures).hasSize(1)
                .extracting(l -> tuple(l.lectureId(), l.lectureTitle()))
                .containsExactly(tuple(myLecture.getId(), "강의1"));
    }

    private LectureEntity buildLectureEntity(String title, String instructorName, LocalDateTime lectureDateTime) {
        return LectureEntity.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }

    private UserLectureEntity buildUserLectureEntity(UserEntity user, LectureEntity lecture) {
        return UserLectureEntity.builder()
                .user(user)
                .lecture(lecture)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
