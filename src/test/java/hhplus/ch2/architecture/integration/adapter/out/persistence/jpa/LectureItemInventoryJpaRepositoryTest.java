package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.*;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LectureItemInventoryJpaRepositoryTest extends DataJpaTestEnvironment {

    @Autowired
    LectureItemInventoryJpaRepository sut;
    
    @Autowired
    InstructorJpaRepository instructorJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;
    
    @Autowired
    LectureItemJpaRepository lectureItemJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;

    @DisplayName("특강 일자 정보로 특강 잔여 좌석 수를 조회할 수 있다.")
    @Test
    void findByLectureItemId() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, thisSaturday, 10L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 10L);
        sut.save(lectureItemInventoryEntity);

        // when
        Optional<LectureItemInventoryEntity> optLectureItemInventory = sut.findByLectureItemId(lectureItemEntity.getId());

        // then
        assertThat(optLectureItemInventory).isPresent();
        LectureItemInventoryEntity lectureItemInventory = optLectureItemInventory.get();
        assertThat(lectureItemInventory.getLectureItemEntity().getId()).isEqualTo(lectureItemEntity.getId());
        assertThat(lectureItemInventory.getLeftSeat()).isEqualTo(10L);
    }

    @DisplayName("특강 일자 정보로 여러 특강 잔여 좌석 수를 한번에 조회할 수 있다.")
    @Test
    void findAllByLectureItemIds() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity1 = buildLectureItemEntity(lectureEntity, thisSaturday, 10L);
        LectureItemEntity lectureItemEntity2 = buildLectureItemEntity(lectureEntity, thisSaturday.plusWeeks(1), 10L);
        lectureItemJpaRepository.saveAll(List.of(lectureItemEntity1, lectureItemEntity2));

        LectureItemInventoryEntity lectureItemInventoryEntity1 = buildLectureItemInventoryEntity(lectureItemEntity1, 10L);
        LectureItemInventoryEntity lectureItemInventoryEntity2 = buildLectureItemInventoryEntity(lectureItemEntity2, 10L);
        sut.saveAll(List.of(lectureItemInventoryEntity1, lectureItemInventoryEntity2));

        // when
        List<LectureItemInventoryEntity> lectureItemInventories = sut.findAllByLectureItemIds(List.of(lectureItemEntity1.getId(), lectureItemEntity2.getId()));

        // then
        assertThat(lectureItemInventories).hasSize(2)
                .extracting(lii -> lii.getLectureItemEntity().getId(), LectureItemInventoryEntity::getLeftSeat)
                .containsExactlyInAnyOrder(
                        tuple(lectureItemEntity1.getId(), 10L),
                        tuple(lectureItemEntity2.getId(), 10L)
                );
    }

    @DisplayName("특강 잔여 좌석 수를 감소시킬 수 있다.")
    @Test
    void decreaseLeftSeatById() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, thisSaturday, 10L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 10L);
        sut.save(lectureItemInventoryEntity);

        flushAndClear();

        // when
        sut.decreaseLeftSeatById(lectureItemInventoryEntity.getId());

        // then
        Optional<LectureItemInventoryEntity> optLectureItemInventory = sut.findByLectureItemId(lectureItemEntity.getId());
        assertThat(optLectureItemInventory).isPresent();
        LectureItemInventoryEntity lectureItemInventory = optLectureItemInventory.get();
        assertThat(lectureItemInventory.getLectureItemEntity().getId()).isEqualTo(lectureItemEntity.getId());
        assertThat(lectureItemInventory.getLeftSeat()).isEqualTo(9L);
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
}
