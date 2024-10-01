package hhplus.ch2.architecture.lecture.application;

import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.FindAvailableLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import hhplus.ch2.architecture.lecture.domain.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FindAvailableLecturesService implements FindAvailableLecturesUseCase {

    private final LecturePort lecturePort;

    @Override
    public List<LectureResponse> findAvailableLectures(FindAvailableLectureCommand command) {
        if (command.lectureDate() == null) {
            return lecturePort.findAllAvailableLectures(Lecture.CAPACITY);
        }
        return lecturePort.findAllAvailableLectures(command.lectureDate(), Lecture.CAPACITY);
    }
}
