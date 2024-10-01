package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    protected UserEntity(Long id) {
        this.id = id;
    }

    public static UserEntity empty() {
        return new UserEntity();
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .build();
    }
}
