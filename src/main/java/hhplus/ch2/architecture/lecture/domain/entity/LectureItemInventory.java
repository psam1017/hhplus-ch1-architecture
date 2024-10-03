package hhplus.ch2.architecture.lecture.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LectureItemInventory {

    private final Long id;
    private final LectureItem lectureItem;
    private final long leftSeat;

    @Builder
    protected LectureItemInventory(Long id, LectureItem lectureItem, long leftSeat) {
        this.id = id;
        this.lectureItem = lectureItem;
        this.leftSeat = leftSeat;
    }
}
