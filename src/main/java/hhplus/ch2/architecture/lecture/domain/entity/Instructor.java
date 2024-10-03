package hhplus.ch2.architecture.lecture.domain.entity;

import lombok.Builder;

public class Instructor extends User {

    @Builder(builderMethodName = "instructorBuilder")
    protected Instructor(Long id, String name) {
        super(id, name);
    }
}
