package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.*;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LectureItemJpaRepositoryTest extends DataJpaTestEnvironment {

    @Autowired
    LectureItemJpaRepository sut;

    @Autowired
    InstructorJpaRepository instructorJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    LectureItemInventoryJpaRepository lectureItemInventoryJpaRepository;

    @DisplayName("수강할 수 있는 모든 강의를 찾을 수 있다.")
    @Test
    void save() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity1 = buildLectureEntity("강의1", instructorEntity);
        LectureEntity lectureEntity2 = buildLectureEntity("강의2", instructorEntity);
        lectureJpaRepository.saveAll(List.of(lectureEntity1, lectureEntity2));

        LectureItemEntity lectureItemEntity1 = buildLectureItemEntity(lectureEntity1, thisSaturday, 10L);
        LectureItemEntity lectureItemEntity2 = buildLectureItemEntity(lectureEntity1, thisSaturday.plusWeeks(1), 10L);
        LectureItemEntity lectureItemEntity3 = buildLectureItemEntity(lectureEntity2, thisSaturday, 10L);
        sut.saveAll(List.of(lectureItemEntity1, lectureItemEntity2, lectureItemEntity3));

        LectureItemInventoryEntity lectureItemInventoryEntity1 = buildLectureItemInventoryEntity(lectureItemEntity1, 10L);
        LectureItemInventoryEntity lectureItemInventoryEntity2 = buildLectureItemInventoryEntity(lectureItemEntity2, 10L);
        LectureItemInventoryEntity lectureItemInventoryEntity3 = buildLectureItemInventoryEntity(lectureItemEntity3, 10L);
        lectureItemInventoryJpaRepository.saveAll(List.of(lectureItemInventoryEntity1, lectureItemInventoryEntity2, lectureItemInventoryEntity3));

        // when
        List<LectureItemEntity> availableLectures = sut.findAllAvailableLectureItems();

        // then
        assertThat(availableLectures).hasSize(3)
                .extracting(l -> tuple(l.getId(), l.getLectureEntity().getId(), l.getCapacity()))
                .containsExactlyInAnyOrder(
                        tuple(lectureItemEntity1.getId(), lectureEntity1.getId(), 10L),
                        tuple(lectureItemEntity2.getId(), lectureEntity1.getId(), 10L),
                        tuple(lectureItemEntity3.getId(), lectureEntity2.getId(), 10L)
                );
    }

    @DisplayName("특정 일자의 강의만 조회할 수 있다.")
    @Test
    void findAllAvailableLectureItems() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity1 = buildLectureEntity("강의1", instructorEntity);
        LectureEntity lectureEntity2 = buildLectureEntity("강의2", instructorEntity);
        lectureJpaRepository.saveAll(List.of(lectureEntity1, lectureEntity2));

        LectureItemEntity lectureItemEntity1 = buildLectureItemEntity(lectureEntity1, LocalDateTime.of(thisSaturday.getYear(), thisSaturday.getMonth(), thisSaturday.getDayOfMonth(), 0, 0, 0), 10L);
        LectureItemEntity lectureItemEntity2 = buildLectureItemEntity(lectureEntity1, thisSaturday.plusWeeks(1), 10L);
        LectureItemEntity lectureItemEntity3 = buildLectureItemEntity(lectureEntity2, LocalDateTime.of(thisSaturday.getYear(), thisSaturday.getMonth(), thisSaturday.getDayOfMonth(), 23, 59, 59), 10L);
        sut.saveAll(List.of(lectureItemEntity1, lectureItemEntity2, lectureItemEntity3));

        LectureItemInventoryEntity lectureItemInventoryEntity1 = buildLectureItemInventoryEntity(lectureItemEntity1, 10L);
        LectureItemInventoryEntity lectureItemInventoryEntity2 = buildLectureItemInventoryEntity(lectureItemEntity2, 10L);
        LectureItemInventoryEntity lectureItemInventoryEntity3 = buildLectureItemInventoryEntity(lectureItemEntity3, 10L);
        lectureItemInventoryJpaRepository.saveAll(List.of(lectureItemInventoryEntity1, lectureItemInventoryEntity2, lectureItemInventoryEntity3));

        // when
        List<LectureItemEntity> availableLectures = sut.findAllAvailableLectureItems(
                LocalDateTime.of(thisSaturday.getYear(), thisSaturday.getMonth(), thisSaturday.getDayOfMonth(), 0, 0, 0),
                LocalDateTime.of(thisSaturday.getYear(), thisSaturday.getMonth(), thisSaturday.getDayOfMonth(), 23, 59, 59)
        );

        // then
        assertThat(availableLectures).hasSize(2)
                .extracting(l -> tuple(l.getId(), l.getLectureEntity().getId(), l.getCapacity()))
                .containsExactlyInAnyOrder(
                        tuple(lectureItemEntity1.getId(), lectureEntity1.getId(), 10L),
                        tuple(lectureItemEntity3.getId(), lectureEntity2.getId(), 10L)
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
}
