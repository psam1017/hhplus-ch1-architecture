package hhplus.ch2.architecture.lecture.application.port.in;

import hhplus.ch2.architecture.lecture.application.response.LectureResponse;

import java.util.List;

public interface FindMyLecturesUseCase {

    List<LectureResponse> findMyLectures(Long userId);
}
