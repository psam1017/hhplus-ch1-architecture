package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, UserLectureEntityId> {

    @Query("""
            SELECT CASE WHEN COUNT(ul) > 0 THEN TRUE ELSE FALSE END
            FROM UserLectureEntity ul
            WHERE ul.userEntity.id = :userId
            AND ul.lectureItemEntity.id = :lectureItemId
            """)
    boolean existsByUserIdAndLectureItemId(Long userId, Long lectureItemId);
}
