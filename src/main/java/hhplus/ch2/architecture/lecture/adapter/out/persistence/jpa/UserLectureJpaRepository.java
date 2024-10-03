package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, UserLectureEntityId> {

}
