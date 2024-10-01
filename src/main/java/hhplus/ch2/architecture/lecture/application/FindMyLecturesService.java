package hhplus.ch2.architecture.lecture.application;

import hhplus.ch2.architecture.lecture.application.port.in.FindMyLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.out.LecturePort;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FindMyLecturesService implements FindMyLecturesUseCase {

    private final LecturePort lecturePort;

    @Override
    public List<LectureResponse> findMyLectures(Long userId) {
        return lecturePort.findAllByUserId(userId);
    }
}
