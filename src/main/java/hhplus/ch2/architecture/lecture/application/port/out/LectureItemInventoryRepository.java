package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LectureItemInventoryRepository {

    Optional<LectureItemInventory> findByLectureItem(LectureItem lectureItem);

    LectureItemInventory decreaseLeftSeatById(LectureItemInventory lectureItemInventory);

    Map<Long, Long> findLeftSeatByLectureItems(List<Long> lectureItemIds);
}
