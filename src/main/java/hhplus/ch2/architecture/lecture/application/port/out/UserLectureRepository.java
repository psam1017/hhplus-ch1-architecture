package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.entity.UserLecture;

public interface UserLectureRepository {

    UserLecture save(UserLecture userLecture);

    boolean existsByUserLecture(UserLecture userLecture);
}
