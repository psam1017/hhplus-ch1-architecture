package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, UserLectureId> {

    long countByLectureId(Long id);
}
