package hhplus.ch2.architecture.lecture.application.port.in;

import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;

import java.util.List;

public interface FindAvailableLecturesUseCase {

    List<LectureResponse> findAvailableLectureItems(FindAvailableLectureCommand command);
}
