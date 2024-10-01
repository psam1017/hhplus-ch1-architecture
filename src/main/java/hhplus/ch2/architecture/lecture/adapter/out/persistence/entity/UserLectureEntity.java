package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.UserLecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserLectureId.class)
@Table(name = "user_lectures")
@Entity
public class UserLectureEntity {

    @Id
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    @JoinColumn(name = "lecture_id")
    private Long lectureId;

    private LocalDateTime createdDateTime;

    @Builder
    protected UserLectureEntity(Long userId, Long lectureId, LocalDateTime createdDateTime) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.createdDateTime = createdDateTime;
    }

    public static UserLectureEntity fromDomain(UserLecture userLecture) {
        return UserLectureEntity.builder()
                .userId(userLecture.getUserId())
                .lectureId(userLecture.getLectureId())
                .createdDateTime(userLecture.getCreatedDateTime())
                .build();
    }

    public UserLecture toDomain() {
        return UserLecture.builder()
                .userId(userId)
                .lectureId(lectureId)
                .createdDateTime(createdDateTime)
                .build();
    }
}
