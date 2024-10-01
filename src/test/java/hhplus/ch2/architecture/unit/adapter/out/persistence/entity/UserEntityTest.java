package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @DisplayName("UserEntity 에서 User 로 변환할 수 있다.")
    @Test
    void toUser() {
        // given
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .build();

        // when
        User user = entity.toDomain();

        // then
        assertThat(user.getId()).isEqualTo(entity.getId());
    }
}
