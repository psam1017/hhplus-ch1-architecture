package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder
    protected UserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
