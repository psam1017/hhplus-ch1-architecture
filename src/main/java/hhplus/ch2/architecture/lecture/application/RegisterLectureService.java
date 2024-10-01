package hhplus.ch2.architecture.lecture.application;

import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.RegisterLectureUseCase;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.application.port.out.UserLecturePort;
import hhplus.ch2.architecture.lecture.application.port.out.UserPort;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchUserException;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.User;
import hhplus.ch2.architecture.lecture.domain.UserLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RegisterLectureService implements RegisterLectureUseCase {

    private final UserPort userPort;
    private final LecturePort lecturePort;
    private final UserLecturePort userLecturePort;

    @Override
    public LectureRegistrationResult registerLecture(RegisterLectureCommand command) {
        Long lectureId = command.lectureId();
        Long userId = command.userId();

        User user = userPort.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
        Lecture lecture = lecturePort.findById(lectureId).orElseThrow(() -> new NoSuchLectureException(lectureId));
        long count = userLecturePort.countByLecture(lecture);
        UserLecture userLecture = lecture.registerUser(count, user);

        // TODO: 2024-09-29 STEP 4 과제
//        if (userLectureRepository.existsByUserAndLecture(user, lecture)) {
//            throw new IllegalArgumentException("Already registered");
//        }
        userLecturePort.save(userLecture);
        return new LectureRegistrationResult(lectureId, userId);
    }
}
