package hhplus.ch2.architecture.lecture.application.command;

import java.time.LocalDate;

public record FindAvailableLectureCommand (
        LocalDate lectureDate
) {

}
