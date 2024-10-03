package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.UserJpaRepository;
import hhplus.ch2.architecture.lecture.domain.entity.User;
import hhplus.ch2.architecture.lecture.application.port.out.UserRepository;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(UserEntity::toDomain);
    }
}
