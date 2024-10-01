package hhplus.ch2.architecture.lecture.domain;

import hhplus.ch2.architecture.lecture.common.exception.LectureDateNotAllowedException;
import hhplus.ch2.architecture.lecture.common.exception.RegisteredCountOverCapacityException;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
public class Lecture {

    // 확정 요구사항 : "정확하게 30 명의 사용자에게만 특강을 제공"
    public static long CAPACITY = 30;

    // 확정 요구사항 : "항해 플러스 토요일 특강을 신청할 수 있는 서비스를 개발"
    private static final DayOfWeek[] ALLOWED_DAYS = {DayOfWeek.SATURDAY};

    private Long id;
    private String title;
    private String instructorName;
    private LocalDateTime lectureDateTime;

    @Builder
    protected Lecture(Long id, String title, String instructorName, LocalDateTime lectureDateTime) {
        if (lectureDateTime == null || !isAllowedDay(lectureDateTime.getDayOfWeek())) {
            throw new LectureDateNotAllowedException(ALLOWED_DAYS);
        }
        this.id = id;
        this.title = title;
        this.instructorName = instructorName;
        this.lectureDateTime = lectureDateTime;
    }

    public UserLecture registerUser(long registeredCount, User user) {
        if (registeredCount >= CAPACITY) {
            throw new RegisteredCountOverCapacityException(CAPACITY);
        }
        return UserLecture.builder()
                .user(user)
                .lecture(this)
                .createdDateTime(LocalDateTime.now())
                .build();
    }

    private boolean isAllowedDay(DayOfWeek dayOfWeek) {
        for (DayOfWeek allowedDay : ALLOWED_DAYS) {
            if (allowedDay.equals(dayOfWeek)) {
                return true;
            }
        }
        return false;
    }
}
