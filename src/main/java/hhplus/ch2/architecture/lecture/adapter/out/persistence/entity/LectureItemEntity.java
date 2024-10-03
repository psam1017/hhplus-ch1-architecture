package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture_items")
@Entity
public class LectureItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lectureDateTime;
    private Long capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;

    @Builder
    protected LectureItemEntity(Long id, LocalDateTime lectureDateTime, Long capacity, LectureEntity lectureEntity) {
        this.id = id;
        this.lectureDateTime = lectureDateTime;
        this.capacity = capacity;
        this.lectureEntity = lectureEntity;
    }

    public static LectureItemEntity fromDomain(LectureItem lectureItem) {
        return LectureItemEntity.builder()
                .id(lectureItem.getId())
                .lectureDateTime(lectureItem.getLectureDateTime())
                .capacity(lectureItem.getCapacity())
                .lectureEntity(LectureEntity.fromDomain(lectureItem.getLecture()))
                .build();
    }

    public LectureItem toDomain() {
        return LectureItem.builder()
                .id(id)
                .lectureDateTime(lectureDateTime)
                .capacity(capacity)
                .lecture(lectureEntity.toDomain())
                .build();
    }
}
