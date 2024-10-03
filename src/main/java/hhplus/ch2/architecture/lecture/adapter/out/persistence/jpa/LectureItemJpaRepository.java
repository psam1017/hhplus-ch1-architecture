package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemEntity;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureItemJpaRepository extends JpaRepository<LectureItemEntity, Long> {


    @Query("""
            SELECT li
            FROM LectureItemEntity li
            JOIN LectureItemInventoryEntity lii ON li.id = lii.lectureItemEntity.id
            WHERE lii.leftSeat > 0
            """)
    List<LectureItemEntity> findAllAvailableLectureItems();

    @Query("""
            SELECT li
            FROM LectureItemEntity li
            JOIN LectureItemInventoryEntity lii ON li.id = lii.lectureItemEntity.id
            WHERE lii.leftSeat > 0
            AND li.lectureDateTime BETWEEN :startDateTime AND :endDateTime
            """)
    List<LectureItemEntity> findAllAvailableLectureItems(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
            SELECT li
            FROM LectureItemEntity li
            JOIN UserLectureEntity ul ON li.id = ul.lectureItemEntity.id
            WHERE ul.userEntity.id = :userId
            """)
    List<LectureItemEntity> findAllByUserId(Long userId);
}
