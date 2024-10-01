package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lectures")
@Entity
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String instructorName;
    private LocalDateTime lectureDateTime;

    @Builder
    protected LectureEntity(Long id, String title, String instructorName, LocalDateTime lectureDateTime) {
        this.id = id;
        this.title = title;
        this.instructorName = instructorName;
        this.lectureDateTime = lectureDateTime;
    }

    public Lecture toDomain() {
        return Lecture.builder()
                .id(id)
                .title(title)
                .instructorName(instructorName)
                .lectureDateTime(lectureDateTime)
                .build();
    }
}
