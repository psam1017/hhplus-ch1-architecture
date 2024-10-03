package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.UserLecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserLectureEntityId.class)
@Table(name = "user_lectures")
@Entity
public class UserLectureEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureItemEntity lectureItemEntity;

    private LocalDateTime createdDateTime;

    @Builder
    protected UserLectureEntity(UserEntity userEntity, LectureItemEntity lectureItemEntity, LocalDateTime createdDateTime) {
        this.userEntity = userEntity;
        this.lectureItemEntity = lectureItemEntity;
        this.createdDateTime = createdDateTime;
    }

    public static UserLectureEntity fromDomain(UserLecture userLecture) {
        return UserLectureEntity.builder()
                .userEntity(UserEntity.fromDomain(userLecture.getUser()))
                .lectureItemEntity(LectureItemEntity.fromDomain(userLecture.getLectureItem()))
                .createdDateTime(userLecture.getCreatedDateTime())
                .build();
    }

    public UserLecture toDomain() {
        return UserLecture.builder()
                .user(userEntity.toDomain())
                .lectureItem(lectureItemEntity.toDomain())
                .createdDateTime(createdDateTime)
                .build();
    }
}
