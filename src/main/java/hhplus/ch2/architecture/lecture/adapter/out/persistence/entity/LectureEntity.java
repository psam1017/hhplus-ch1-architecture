package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lectures")
@Entity
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private InstructorEntity instructorEntity;

    @Builder
    protected LectureEntity(Long id, String title, InstructorEntity instructorEntity) {
        this.id = id;
        this.title = title;
        this.instructorEntity = instructorEntity;
    }

    public Lecture toDomain() {
        return Lecture.builder()
                .id(id)
                .title(title)
                .instructor(instructorEntity.toDomain())
                .build();
    }

    public static LectureEntity fromDomain(Lecture lecture) {
        return LectureEntity.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .instructorEntity(InstructorEntity.fromDomain(lecture.getInstructor()))
                .build();
    }
}
