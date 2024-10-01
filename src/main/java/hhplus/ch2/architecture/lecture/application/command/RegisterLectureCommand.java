package hhplus.ch2.architecture.lecture.application.command;

public record RegisterLectureCommand(
        Long userId,
        Long lectureId
) {

}
