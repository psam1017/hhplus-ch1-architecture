package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserLectureJpaRepositoryTest extends DataJpaTestEnvironment {

    @Autowired
    UserLectureJpaRepository sut;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @DisplayName("사용자를 강의 수강생으로 등록할 수 있다.")
    @Test
    void save() {
        // given
        LectureEntity buildLectureEntity = buildLectureEntity("강의 제목", "강사 이름", LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0));
        LectureEntity saveLectureEntity = lectureJpaRepository.save(buildLectureEntity);
        UserEntity saveUserEntity = userJpaRepository.save(UserEntity.empty());
        UserLectureEntity userLectureEntity = buildUserLectureEntity(saveUserEntity.getId(), saveLectureEntity.getId());

        // when
        UserLectureEntity saveUserLectureEntity = sut.save(userLectureEntity);

        // then
        assertThat(saveUserLectureEntity.getUserId()).isEqualTo(userLectureEntity.getUserId());
        assertThat(saveUserLectureEntity.getLectureId()).isEqualTo(userLectureEntity.getLectureId());
        assertThat(saveUserLectureEntity.getCreatedDateTime()).isEqualTo(userLectureEntity.getCreatedDateTime());
    }

    @DisplayName("현재 강의를 듣고 있는 사용자 수를 조회할 수 있다.")
    @Test
    void countByLecture() {
        // given
        LectureEntity buildLectureEntity = buildLectureEntity("강의 제목", "강사 이름", LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0));
        LectureEntity saveLectureEntity = lectureJpaRepository.save(buildLectureEntity);
        UserEntity saveUserEntity = userJpaRepository.save(UserEntity.empty());

        UserLectureEntity userLectureEntity = buildUserLectureEntity(saveUserEntity.getId(), saveLectureEntity.getId());
        sut.save(userLectureEntity);

        // when
        long count = sut.countByLectureId(saveLectureEntity.getId());

        // then
        assertThat(count).isEqualTo(1);
    }

    private LectureEntity buildLectureEntity(String title, String instructorName, LocalDateTime lectureDateTime) {
        return LectureEntity.builder()
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }

    private UserLectureEntity buildUserLectureEntity(Long userId, Long lectureId) {
        return UserLectureEntity.builder()
                .userId(userId)
                .lectureId(lectureId)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
