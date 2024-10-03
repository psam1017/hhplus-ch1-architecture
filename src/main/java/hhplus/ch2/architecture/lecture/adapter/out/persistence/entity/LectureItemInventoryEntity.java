package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture_item_inventories")
@Entity
public class LectureItemInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "lecture_item_id")
    private LectureItemEntity lectureItemEntity;

    private Long leftSeat;

    @Builder
    protected LectureItemInventoryEntity(Long id, LectureItemEntity lectureItemEntity, Long leftSeat) {
        this.id = id;
        this.lectureItemEntity = lectureItemEntity;
        this.leftSeat = leftSeat;
    }

    public static LectureItemInventoryEntity fromDomain(LectureItemInventory lectureItemInventory) {
        return LectureItemInventoryEntity.builder()
                .id(lectureItemInventory.getId())
                .lectureItemEntity(LectureItemEntity.fromDomain(lectureItemInventory.getLectureItem()))
                .leftSeat(lectureItemInventory.getLeftSeat())
                .build();
    }

    public LectureItemInventory toDomain() {
        return LectureItemInventory.builder()
                .id(id)
                .lectureItem(lectureItemEntity.toDomain())
                .leftSeat(leftSeat)
                .build();
    }
}
