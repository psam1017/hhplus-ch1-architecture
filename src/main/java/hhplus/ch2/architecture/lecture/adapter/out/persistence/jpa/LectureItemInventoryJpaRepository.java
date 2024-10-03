package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemInventoryEntity;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LectureItemInventoryJpaRepository extends JpaRepository<LectureItemInventoryEntity, Long> {

    @Query("""
            SELECT lii
            FROM LectureItemInventoryEntity lii
            WHERE lii.lectureItemEntity.id = :lectureItemId
            """)
    Optional<LectureItemInventoryEntity> findByLectureItemId(Long lectureItemId);

    @Query("""
            SELECT lii
            FROM LectureItemInventoryEntity lii
            WHERE lii.lectureItemEntity.id IN :lectureItemIds
            """)
    List<LectureItemInventoryEntity> findAllByLectureItemIds(List<Long> lectureItemIds);

    @Modifying(flushAutomatically = true)
    @Query("""
            UPDATE LectureItemInventoryEntity lii
            SET lii.leftSeat = lii.leftSeat - 1
            WHERE lii.id = :lectureItemInventoryId
            """)
    void decreaseLeftSeatById(Long lectureItemInventoryId);
}
