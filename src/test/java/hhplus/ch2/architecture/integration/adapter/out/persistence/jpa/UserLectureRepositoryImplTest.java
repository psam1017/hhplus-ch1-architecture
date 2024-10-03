package hhplus.ch2.architecture.integration.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserLectureRepositoryImplTest extends DataJpaTestEnvironment {

    @Autowired
    UserLectureJpaRepository sut;
    
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

    @DisplayName("사용자를 강의 수강생으로 등록할 수 있다.")
    @Test
    void save() {
        // given
        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, LocalDateTime.now().with(DayOfWeek.SATURDAY), 10L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 10L);
        lectureItemInventoryJpaRepository.save(lectureItemInventoryEntity);

        UserEntity userEntity = buildUserEntity("사용자1");
        userJpaRepository.save(userEntity);

        UserLectureEntity userLectureEntity = UserLectureEntity.builder()
                .userEntity(userEntity)
                .lectureItemEntity(lectureItemEntity)
                .createdDateTime(LocalDateTime.now())
                .build();

        // when
        UserLectureEntity saveUserLectureEntity = sut.save(userLectureEntity);

        // then
        assertThat(saveUserLectureEntity).isNotNull();
        assertThat(saveUserLectureEntity.getUserEntity().getId()).isEqualTo(userEntity.getId());
        assertThat(saveUserLectureEntity.getLectureItemEntity().getId()).isEqualTo(lectureItemEntity.getId());
    }

    @DisplayName("사용자가 강의를 이미 신청했는지 여부를 알 수 있다.")
    @Test
    void existsByUserIdAndLectureItemId() {
        // given
        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, LocalDateTime.now().with(DayOfWeek.SATURDAY), 10L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 10L);
        lectureItemInventoryJpaRepository.save(lectureItemInventoryEntity);

        UserEntity userEntity = buildUserEntity("사용자1");
        userJpaRepository.save(userEntity);

        UserLectureEntity userLectureEntity = UserLectureEntity.builder()
                .userEntity(userEntity)
                .lectureItemEntity(lectureItemEntity)
                .createdDateTime(LocalDateTime.now())
                .build();
        sut.save(userLectureEntity);

        // when
        boolean exists = sut.existsByUserIdAndLectureItemId(userEntity.getId(), lectureItemEntity.getId());

        // then
        assertThat(exists).isTrue();
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
