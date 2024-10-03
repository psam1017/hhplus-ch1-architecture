package hhplus.ch2.architecture.lecture.application.port.out;

import hhplus.ch2.architecture.lecture.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
}
