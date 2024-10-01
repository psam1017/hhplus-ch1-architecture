package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
