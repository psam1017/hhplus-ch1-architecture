package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.InstructorEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemInventoryEntity;
import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class LectureItemInventoryTest {

    @DisplayName("LectureItemInventory 에서 LectureItemInventoryEntity 로 변환할 수 있다.")
    @Test
    void fromDomain() {
        // given
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        Lecture lecture = Lecture.builder()
                .id(1L)
                .title("강의 제목")
                .instructor(instructor)
                .build();
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lecture(lecture)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .build();
        LectureItemInventory lectureItemInventory = LectureItemInventory.builder()
                .id(1L)
                .lectureItem(lectureItem)
                .leftSeat(30L)
                .build();

        // when
        LectureItemInventoryEntity entity = LectureItemInventoryEntity.fromDomain(lectureItemInventory);

        // then
        assertThat(entity.getId()).isEqualTo(lectureItemInventory.getId());
        assertThat(entity.getLectureItemEntity().getId()).isEqualTo(lectureItemInventory.getLectureItem().getId());
        assertThat(entity.getLeftSeat()).isEqualTo(lectureItemInventory.getLeftSeat());
    }

    @DisplayName("LectureItemInventoryEntity 에서 LectureItemInventory 로 변환할 수 있다.")
    @Test
    void toDomain() {
        // given
        InstructorEntity instructorEntity = InstructorEntity.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        LectureEntity lectureEntity = LectureEntity.builder()
                .id(1L)
                .title("강의 제목")
                .instructorEntity(instructorEntity)
                .build();
        LectureItemEntity lectureItemEntity = LectureItemEntity.builder()
                .id(1L)
                .lectureEntity(lectureEntity)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .build();
        LectureItemInventoryEntity entity = LectureItemInventoryEntity.builder()
                .id(1L)
                .lectureItemEntity(lectureItemEntity)
                .leftSeat(30L)
                .build();

        // when
        LectureItemInventory lectureItemInventory = entity.toDomain();

        // then
        assertThat(lectureItemInventory.getId()).isEqualTo(entity.getId());
        assertThat(lectureItemInventory.getLectureItem().getId()).isEqualTo(entity.getLectureItemEntity().getId());
        assertThat(lectureItemInventory.getLeftSeat()).isEqualTo(entity.getLeftSeat());
    }
}
/*

    @Test
    void fromLectureItem() {
        // given
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        Lecture lecture = Lecture.builder()
                .id(1L)
                .title("강의 제목")
                .instructor(instructor)
                .build();
        LectureItem lectureItem = LectureItem.builder()
                .id(1L)
                .lecture(lecture)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .build();

        // when
        LectureItemEntity entity = LectureItemEntity.fromDomain(lectureItem);

        // then
        assertThat(entity.getId()).isEqualTo(lectureItem.getId());
        assertThat(entity.getLectureEntity().getId()).isEqualTo(lectureItem.getLecture().getId());
        assertThat(entity.getLectureDateTime()).isEqualTo(lectureItem.getLectureDateTime());
        assertThat(entity.getCapacity()).isEqualTo(lectureItem.getCapacity());
    }

    @DisplayName("LectureItemEntity 에서 LectureItem 로 변환할 수 있다.")
    @Test
    void toLectureItem() {
        // given
        InstructorEntity instructorEntity = InstructorEntity.instructorBuilder()
                .id(1L)
                .name("강사 이름")
                .build();
        LectureEntity lectureEntity = LectureEntity.builder()
                .id(1L)
                .title("강의 제목")
                .instructorEntity(instructorEntity)
                .build();
        LectureItemEntity entity = LectureItemEntity.builder()
                .id(1L)
                .lectureEntity(lectureEntity)
                .lectureDateTime(LocalDateTime.now())
                .capacity(30L)
                .build();

        // when
        LectureItem lectureItem = entity.toDomain();

        // then
        assertThat(lectureItem.getId()).isEqualTo(entity.getId());
        assertThat(lectureItem.getLecture().getId()).isEqualTo(entity.getLectureEntity().getId());
        assertThat(lectureItem.getLectureDateTime()).isEqualTo(entity.getLectureDateTime());
        assertThat(lectureItem.getCapacity()).isEqualTo(entity.getCapacity());
    }
 */