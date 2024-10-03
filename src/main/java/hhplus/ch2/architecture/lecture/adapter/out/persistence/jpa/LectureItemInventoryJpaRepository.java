package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemInventoryEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface LectureItemInventoryJpaRepository extends JpaRepository<LectureItemInventoryEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")
    })
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
