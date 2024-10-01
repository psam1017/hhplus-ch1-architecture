package hhplus.ch2.architecture.lecture.application.port.in;

import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;

public interface RegisterLectureUseCase {

    LectureRegistrationResult registerLecture(RegisterLectureCommand command);
}
