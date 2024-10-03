package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorJpaRepository extends JpaRepository<InstructorEntity, Long> {
}
