package hhplus.ch2.architecture.lecture.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final String name;

    @Builder
    protected User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
