package hhplus.ch2.architecture.lecture.adapter.out.persistence.entity;

import hhplus.ch2.architecture.lecture.domain.entity.Instructor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("INSTRUCTOR")
@Entity
public class InstructorEntity extends UserEntity {

    @Builder(builderMethodName = "instructorBuilder")
    protected InstructorEntity(Long id, String name) {
        super(id, name);
    }

    public Instructor toDomain() {
        return Instructor.instructorBuilder()
                .id(this.getId())
                .name(this.getName())
                .build();
    }

    public static InstructorEntity fromDomain(Instructor instructor) {
        return InstructorEntity.instructorBuilder()
                .id(instructor.getId())
                .name(instructor.getName())
                .build();
    }
}
