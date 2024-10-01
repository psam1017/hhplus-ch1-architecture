package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.Lecture;
import hhplus.ch2.architecture.lecture.domain.User;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lecture;

    private LocalDateTime createdDateTime;

    @Builder
    protected UserLectureEntity(UserEntity user, LectureEntity lecture, LocalDateTime createdDateTime) {
        this.user = user;
        this.lecture = lecture;
        this.createdDateTime = createdDateTime;
    }

    public static UserLectureEntity fromDomain(UserLecture userLecture) {
        return UserLectureEntity.builder()
                .user(UserEntity.fromDomain(userLecture.getUser()))
                .lecture(LectureEntity.fromDomain(userLecture.getLecture()))
                .createdDateTime(userLecture.getCreatedDateTime())
                .build();
    }

    public UserLecture toDomain() {
        return UserLecture.builder()
                .user(user.toDomain())
                .lecture(lecture.toDomain())
                .createdDateTime(createdDateTime)
                .build();
    }
}
