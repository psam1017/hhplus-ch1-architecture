package hhplus.ch2.architecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;

    @Builder
    protected User(Long id) {
        this.id = id;
    }
}
