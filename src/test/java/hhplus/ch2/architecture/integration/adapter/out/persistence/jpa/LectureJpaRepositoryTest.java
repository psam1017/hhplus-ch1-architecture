package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserJpaRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LectureJpaRepositoryTest extends DataJpaTestEnvironment {

    @Autowired
    LectureJpaRepository sut;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;
    
    @DisplayName("강의를 조회할 수 있다.")
    @Test
    void findById() {
        // given
        LectureEntity buildLecture = buildLectureEntity("강의 제목", "강사 이름", LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0));
        LectureEntity saveLecture = sut.save(buildLecture);

        // when
        LectureEntity findLecture = sut.findById(saveLecture.getId()).orElseThrow();

        // then
        assertThat(findLecture.getId()).isEqualTo(saveLecture.getId());
        assertThat(findLecture.getTitle()).isEqualTo(saveLecture.getTitle());
        assertThat(findLecture.getInstructorName()).isEqualTo(saveLecture.getInstructorName());
        assertThat(findLecture.getLectureDateTime()).isEqualTo(saveLecture.getLectureDateTime());
    }

    @DisplayName("특정 일자와 상관 없이 수강할 수 있는 모든 강의를 조회할 수 있다.")
    @Test
    void findAllAvailableLectures() {
        // given
        LocalDate thisSaturday = LocalDate.now().with(DayOfWeek.SATURDAY);

        LectureEntity thisSaturdayLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.atTime(10, 0));
        LectureEntity nextSaturdayLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.plusWeeks(1).atTime(10, 0));
        sut.saveAll(List.of(thisSaturdayLecture, nextSaturdayLecture));

        // when
        List<LectureResponse> lectures = sut.findAllByRegisteredCountLessThan(30);

        // then
        assertThat(lectures).hasSize(2)
                .extracting(LectureResponse::lectureId)
                .containsExactlyInAnyOrder(thisSaturdayLecture.getId(), nextSaturdayLecture.getId());
    }
    
    @DisplayName("특정 일자의 강의를 조회할 수 있다.")
    @Test
    void findAllAvailableLecturesByDate() {
        // given
        LocalDate thisSaturday = LocalDate.now().with(DayOfWeek.SATURDAY);

        LectureEntity thisSaturdayLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.atTime(10, 0));
        LectureEntity nextSaturdayLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.plusWeeks(1).atTime(10, 0));
        sut.saveAll(List.of(thisSaturdayLecture, nextSaturdayLecture));
        
        // when
        List<LectureResponse> lectures = sut.findAllByLectureDateTimeBetweenAndRegisteredCountLessThan(
                thisSaturday.atStartOfDay(),
                thisSaturday.atTime(23, 59, 59),
                30
        );

        // then
        assertThat(lectures).hasSize(1)
                .extracting(LectureResponse::lectureId)
                .containsExactly(thisSaturdayLecture.getId());
    }

    @DisplayName("수강 신청 제한 인원을 초과하지 않은 강좌만 조회할 수 있다.")
    @Test
    void findAllAvailableLecturesByDateAndCapacity() {
        // given
        LocalDate thisSaturday = LocalDate.now().with(DayOfWeek.SATURDAY);

        LectureEntity availableLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.atTime(10, 0));
        LectureEntity fullLecture = buildLectureEntity("강의 제목", "강사 이름", thisSaturday.atTime(10, 0));
        sut.saveAll(List.of(availableLecture, fullLecture));

        List<UserLectureEntity> userLectureEntities = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            UserEntity userEntity = userJpaRepository.save(UserEntity.empty());
            UserLectureEntity userLectureEntity = buildUserLectureEntity(userEntity, fullLecture);
            userLectureEntities.add(userLectureEntity);
        }
        userLectureJpaRepository.saveAll(userLectureEntities);

        flushAndClear();

        // when
        List<LectureResponse> lectures = sut.findAllByLectureDateTimeBetweenAndRegisteredCountLessThan(
                thisSaturday.atStartOfDay(),
                thisSaturday.atTime(23, 59, 59),
                30
        );

        // then
        assertThat(lectures).hasSize(1)
                .extracting(LectureResponse::lectureId)
                .containsExactly(availableLecture.getId());
    }

    @DisplayName("특정 사용자가 수강한 강의를 조회할 수 있다.")
    @Test
    void findAllByUserId() {
        // given
        LectureEntity myLecture = buildLectureEntity("강의 제목", "강사 이름", LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0));
        LectureEntity anotherLecture = buildLectureEntity("강의 제목", "강사 이름", LocalDate.now().with(DayOfWeek.SATURDAY).atTime(10, 0));
        sut.saveAll(List.of(myLecture, anotherLecture));

        UserEntity saveUserEntity = userJpaRepository.save(UserEntity.empty());
        UserLectureEntity userLectureEntity = buildUserLectureEntity(saveUserEntity, myLecture);
        userLectureJpaRepository.save(userLectureEntity);

        // when
        List<LectureResponse> lectures = sut.findAllByUserId(saveUserEntity.getId());

        // then
        assertThat(lectures).hasSize(1)
                .extracting(LectureResponse::lectureId)
                .containsExactly(myLecture.getId());
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
