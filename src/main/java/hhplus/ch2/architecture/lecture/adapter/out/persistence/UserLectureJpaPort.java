package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.UserLecture;
import hhplus.ch2.architecture.lecture.application.port.out.UserLecturePort;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Repository
public class UserLectureJpaPort implements UserLecturePort {

    private final UserLectureJpaRepository userLectureJpaRepository;

    @Override
    public long countByLecture(Lecture lecture) {
        return userLectureJpaRepository.countByLectureId(lecture.getId());
    }

    @Override
    public UserLecture save(UserLecture userLecture) {
        return userLectureJpaRepository.save(UserLectureEntity.fromDomain(userLecture)).toDomain();
    }
}
