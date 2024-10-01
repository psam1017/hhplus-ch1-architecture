package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.UserLecture;

public interface UserLecturePort {
    long countByLecture(Lecture lecture);

    UserLecture save(UserLecture userLecture);
}
