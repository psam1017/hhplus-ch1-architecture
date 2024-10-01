package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Repository
public class LectureJpaPort implements LecturePort {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return lectureJpaRepository.findById(lectureId).map(LectureEntity::toDomain);
    }

    @Override
    public List<LectureResponse> findAllAvailableLectures(long capacity) {
        return lectureJpaRepository.findAllByRegisteredCountLessThan(capacity);
    }

    @Override
    public List<LectureResponse> findAllAvailableLectures(LocalDate lectureDate, long capacity) {
        return lectureJpaRepository.findAllByLectureDateTimeBetweenAndRegisteredCountLessThan(
                        lectureDate.atStartOfDay(),
                        lectureDate.atTime(23, 59, 59),
                        capacity
                );
    }

    @Override
    public List<LectureResponse> findAllByUserId(Long userId) {
        return lectureJpaRepository.findAllByUserId(userId);
    }
}
