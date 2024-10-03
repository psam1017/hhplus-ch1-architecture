package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserLectureJpaRepository;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import hhplus.ch2.architecture.lecture.domain.entity.UserLecture;
import hhplus.ch2.architecture.lecture.application.port.out.UserLectureRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserLectureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Repository
public class UserLectureRepositoryImpl implements UserLectureRepository {

    private final UserLectureJpaRepository userLectureJpaRepository;

    @Override
    public UserLecture save(UserLecture userLecture) {
        return userLectureJpaRepository.save(UserLectureEntity.fromDomain(userLecture)).toDomain();
    }
}
