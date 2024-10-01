package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.User;

import java.util.Optional;

public interface UserPort {
    Optional<User> findById(Long userId);
}
