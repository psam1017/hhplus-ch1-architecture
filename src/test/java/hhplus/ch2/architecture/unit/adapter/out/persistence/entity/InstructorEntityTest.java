package hhplus.ch2.architecture.unit.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.UserEntity;
import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import hhplus.ch2.architecture.lecture.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstructorEntityTest {

    @DisplayName("Instructor 에서 InstructorEntity 로 변환할 수 있다.")
    @Test
    void fromInstructor() {
        // given
        Instructor instructor = Instructor.instructorBuilder()
                .id(1L)
                .name("강사1")
                .build();

        // when
        UserEntity entity = UserEntity.fromDomain(instructor);

        // then
        assertThat(entity.getId()).isEqualTo(instructor.getId());
        assertThat(entity.getName()).isEqualTo(instructor.getName());
    }

    @DisplayName("InstructorEntity 에서 Instructor 로 변환할 수 있다.")
    @Test
    void toInstructor() {
        // given
        UserEntity entity = UserEntity.builder()
                .id(1L)
                .name("강사1")
                .build();

        // when
        User user = entity.toDomain();

        // then
        assertThat(user.getId()).isEqualTo(entity.getId());
        assertThat(user.getName()).isEqualTo(entity.getName());
    }
}
