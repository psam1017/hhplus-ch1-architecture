package hhplus.ch2.architecture.unit.adapter.in.web.model.request;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.request.LectureRegistrationForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LectureRegistrationFormTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

    @DisplayName("특강 수강 신청을 위해서는 사용자의 아이디를 제출해야 한다.")
    @Test
    void userIdNotNull() {
        // given
        LectureRegistrationForm form = new LectureRegistrationForm(null);

        // when
        Set<ConstraintViolation<LectureRegistrationForm>> validations = validator.validate(form);

        // then
        assertThat(validations).hasSize(1)
                .extracting(cv -> tuple(cv.getPropertyPath().toString(), cv.getConstraintDescriptor().getAnnotation().annotationType()))
                .contains(tuple("userId", NotNull.class));
    }
}
