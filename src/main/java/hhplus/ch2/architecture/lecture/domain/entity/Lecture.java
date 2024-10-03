package hhplus.ch2.architecture.lecture.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Lecture {

    private final Long id;
    private final String title;
    private final Instructor instructor;

    @Builder
    protected Lecture(Long id, String title, Instructor instructor) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
    }
}
