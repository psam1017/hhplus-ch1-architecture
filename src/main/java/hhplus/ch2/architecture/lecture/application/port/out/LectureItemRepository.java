package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureItemRepository {

    Optional<LectureItem> findById(Long lectureItemId);

    List<LectureItem> findAllAvailableLectureItems();

    List<LectureItem> findAllAvailableLectureItems(LocalDate localDate);

    List<LectureItem> findAllByUserId(Long userId);
}
