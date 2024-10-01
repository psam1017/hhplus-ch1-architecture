package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import hhplus.ch2.architecture.lecture.domain.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LecturePort {
    Optional<Lecture> findById(Long lectureId);

    List<LectureResponse> findAllAvailableLectures(long capacity);

    List<LectureResponse> findAllAvailableLectures(LocalDate lectureDate, long capacity);

    List<LectureResponse> findAllByUserId(Long userId);
}
