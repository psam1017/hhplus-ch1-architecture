package hhplus.ch2.architecture.integration.application.port.in;

import hhplus.ch2.architecture.integration.application.SpringBootTestEnvironment;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.*;
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
    InstructorJpaRepository instructorJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    LectureItemJpaRepository lectureItemJpaRepository;

    @Autowired
    LectureItemInventoryJpaRepository lectureItemInventoryJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;

    @DisplayName("내가 신청한 강의를 찾을 수 있다.")
    @Test
    void findMyLectures() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity1 = buildLectureEntity("강의1", instructorEntity);
        LectureEntity lectureEntity2 = buildLectureEntity("강의2", instructorEntity);
        lectureJpaRepository.saveAll(List.of(lectureEntity1, lectureEntity2));

        LectureItemEntity myLectureItem = buildLectureItemEntity(lectureEntity1, thisSaturday, 10L);
        LectureItemEntity anotherLectureItem = buildLectureItemEntity(lectureEntity1, thisSaturday.plusWeeks(1), 10L);
        lectureItemJpaRepository.saveAll(List.of(myLectureItem, anotherLectureItem));

        LectureItemInventoryEntity lectureItemInventoryEntity1 = buildLectureItemInventoryEntity(myLectureItem, 9L);
        LectureItemInventoryEntity lectureItemInventoryEntity2 = buildLectureItemInventoryEntity(anotherLectureItem, 10L);
        lectureItemInventoryJpaRepository.saveAll(List.of(lectureItemInventoryEntity1, lectureItemInventoryEntity2));

        UserEntity userEntity = buildUserEntity("user1");
        userJpaRepository.save(userEntity);

        UserLectureEntity userLectureEntity = buildUserLectureEntity(userEntity, myLectureItem);
        userLectureJpaRepository.save(userLectureEntity);

        // when
        List<LectureResponse> lectures = sut.findMyLectures(userEntity.getId());

        // then
        assertThat(lectures).hasSize(1)
                .extracting(l -> tuple(l.lectureId(), l.lectureTitle(), l.instructorName(), l.leftSeat(), l.capacity()))
                .containsExactlyInAnyOrder(
                        tuple(myLectureItem.getId(), "강의1", "강사1", 9L, 10L)
                );
    }

    private InstructorEntity buildInstructorEntity(String name) {
        return InstructorEntity.instructorBuilder()
                .name(name)
                .build();
    }

    private LectureEntity buildLectureEntity(String title, InstructorEntity instructorEntity) {
        return LectureEntity.builder()
                .title(title)
                .instructorEntity(instructorEntity)
                .build();
    }

    private LectureItemEntity buildLectureItemEntity(LectureEntity lectureEntity, LocalDateTime lectureDateTime, Long capacity) {
        return LectureItemEntity.builder()
                .lectureEntity(lectureEntity)
                .lectureDateTime(lectureDateTime)
                .capacity(capacity)
                .build();
    }

    private LectureItemInventoryEntity buildLectureItemInventoryEntity(LectureItemEntity lectureItemEntity, Long leftSeat) {
        return LectureItemInventoryEntity.builder()
                .lectureItemEntity(lectureItemEntity)
                .leftSeat(leftSeat)
                .build();
    }

    private UserEntity buildUserEntity(String name) {
        return UserEntity.builder()
                .name(name)
                .build();
    }

    private UserLectureEntity buildUserLectureEntity(UserEntity userEntity, LectureItemEntity lectureItemEntity) {
        return UserLectureEntity.builder()
                .userEntity(userEntity)
                .lectureItemEntity(lectureItemEntity)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
