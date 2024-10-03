package hhplus.ch2.architecture.lecture.domain.entity;

import hhplus.ch2.architecture.lecture.common.exception.LectureItemOutOfLeftSeatException;
import hhplus.ch2.architecture.lecture.domain.enumeration.LectureCapacity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LectureItem {

    private final Long id;
    private final Lecture lecture;
    private final LocalDateTime lectureDateTime;
    private final long capacity;

    @Builder
    protected LectureItem(Long id, Lecture lecture, LocalDateTime lectureDateTime, Long capacity) {
        this.id = id;
        this.lecture = lecture;
        this.lectureDateTime = lectureDateTime;
        this.capacity = capacity != null ? capacity : LectureCapacity.DEFAULT.value();
    }

    public UserLecture registerUser(long leftSeat, User user) {
        if (leftSeat <= 0) {
            throw new LectureItemOutOfLeftSeatException();
        }
        return UserLecture.builder()
                .user(user)
                .lectureItem(this)
                .createdDateTime(LocalDateTime.now())
                .build();
    }
}
