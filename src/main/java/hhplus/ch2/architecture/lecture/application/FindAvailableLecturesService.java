package hhplus.ch2.architecture.lecture.application;

import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.FindAvailableLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemInventoryRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemRepository;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class FindAvailableLecturesService implements FindAvailableLecturesUseCase {

    private final LectureItemRepository lectureItemRepository;
    private final LectureItemInventoryRepository lectureItemInventoryRepository;

    @Override
    public List<LectureResponse> findAvailableLectureItems(FindAvailableLectureCommand command) {
        List<LectureItem> lectureItems = command.lectureDate() == null
                ? lectureItemRepository.findAllAvailableLectureItems()
                : lectureItemRepository.findAllAvailableLectureItems(command.lectureDate());
        Map<Long, Long> leftSeatMap = lectureItemInventoryRepository.findLeftSeatByLectureItems(lectureItems.stream().map(LectureItem::getId).toList());

        return lectureItems.stream()
                .map(lectureItem -> makeResponse(lectureItem, leftSeatMap.get(lectureItem.getId())))
                .toList();
    }

    private LectureResponse makeResponse(LectureItem lectureItem, Long leftSeat) {
        Lecture lecture = lectureItem.getLecture();
        String title = lecture != null ? lecture.getTitle() : null;
        String instructorName = lecture != null && lecture.getInstructor() != null ? lecture.getInstructor().getName() : null;
        return new LectureResponse(
                lectureItem.getId(),
                title,
                instructorName,
                lectureItem.getLectureDateTime(),
                leftSeat,
                lectureItem.getCapacity()
        );
    }
}
