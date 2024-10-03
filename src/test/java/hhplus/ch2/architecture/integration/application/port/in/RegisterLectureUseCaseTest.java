package hhplus.ch2.architecture.integration.application.port.in;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.*;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.*;
import hhplus.ch2.architecture.lecture.application.RegisterLectureService;
import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import hhplus.ch2.architecture.lecture.common.exception.LectureItemOutOfLeftSeatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


// 동시성 테스트에 트랜잭션 전파가 영향을 주지 않도록 @Transactional 을 사용하지 않습니다.
@SpringBootTest
public class RegisterLectureUseCaseTest {

    @Autowired
    RegisterLectureService sut;

    @Autowired
    InstructorJpaRepository instructorJpaRepository;

    @Autowired
    LectureJpaRepository lectureJpaRepository;

    @Autowired
    LectureItemJpaRepository lectureItemJpaRepository;

    @Autowired
    LectureItemInventoryJpaRepository lectureItemInventoryJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserLectureJpaRepository userLectureJpaRepository;

    @AfterEach
    void tearDown() {
        userLectureJpaRepository.deleteAll();
        lectureItemInventoryJpaRepository.deleteAll();
        lectureItemJpaRepository.deleteAll();
        lectureJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @DisplayName("사용자가 강의를 신청할 수 있다.")
    @Test
    void registerLecture() {
        // given
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, thisSaturday, 10L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 10L);
        lectureItemInventoryJpaRepository.save(lectureItemInventoryEntity);

        UserEntity userEntity = buildUserEntity("사용자1");
        userJpaRepository.save(userEntity);

        RegisterLectureCommand command = new RegisterLectureCommand(userEntity.getId(), lectureItemEntity.getId());

        // when
        LectureRegistrationResult result = sut.registerLecture(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.lectureItemId()).isEqualTo(lectureItemEntity.getId());
        assertThat(result.userId()).isEqualTo(userEntity.getId());

        Optional<UserLectureEntity> optUserLectureEntity = userLectureJpaRepository.findById(buildUserLectureId(userEntity, lectureItemEntity));
        assertThat(optUserLectureEntity).isPresent();
        UserLectureEntity userLectureEntity = optUserLectureEntity.get();
        assertThat(userLectureEntity.getUserEntity().getId()).isEqualTo(userEntity.getId());
        assertThat(userLectureEntity.getLectureItemEntity().getId()).isEqualTo(lectureItemEntity.getId());
    }

    /**
     * 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공할 수 있는지를 검증하는 테스트입니다.
     * 이 테스트를 단독으로 실행했을 때, gradle 과 관련한 이슈로 테스트는 성공하나, 빌드가 종료되지 않는 이슈가 발생할 수 있습니다(사용자 환경마다 다릅니다).
     * 만약 그런 경우에는 콘솔 창에 yes 를 입력하십시오.
     * 아니면, 속 편하게 다른 테스트랑 같이 실행하십시오. 그러면 빌드가 정상 종료됩니다.
     */
    @DisplayName("동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공할 수 있다.")
    @Test
    void registerLectureWhen40PeopleApply() throws InterruptedException {
        // given
        InstructorEntity instructorEntity = buildInstructorEntity("강사1");
        instructorJpaRepository.save(instructorEntity);

        LectureEntity lectureEntity = buildLectureEntity("강의1", instructorEntity);
        lectureJpaRepository.save(lectureEntity);

        LectureItemEntity lectureItemEntity = buildLectureItemEntity(lectureEntity, LocalDateTime.now(), 30L);
        lectureItemJpaRepository.save(lectureItemEntity);

        LectureItemInventoryEntity lectureItemInventoryEntity = buildLectureItemInventoryEntity(lectureItemEntity, 30L);
        LectureItemInventoryEntity lectureItemInventory = lectureItemInventoryJpaRepository.save(lectureItemInventoryEntity);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(40);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(40);

        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            UserEntity userEntity = userJpaRepository.save(buildUserEntity("사용자" + i));
            tasks.add(() -> {
                try {
                    startLatch.await();
                    sut.registerLecture(new RegisterLectureCommand(userEntity.getId(), lectureItemEntity.getId()));
                    successCount.incrementAndGet();
                } catch (LectureItemOutOfLeftSeatException e) {
                    failCount.incrementAndGet();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    endLatch.countDown();
                }
            });
        }
        tasks.forEach(executorService::submit);

        // when
        startLatch.countDown();
        endLatch.await();

        // then
        assertThat(successCount.get()).isEqualTo(30);
        assertThat(failCount.get()).isEqualTo(10);

        LectureItemInventoryEntity updatedLectureItemInventoryEntity = lectureItemInventoryJpaRepository.findById(lectureItemInventory.getId()).orElseThrow();
        assertThat(updatedLectureItemInventoryEntity.getLeftSeat()).isEqualTo(0);

        long count = userLectureJpaRepository.count();
        assertThat(count).isEqualTo(30);

        executorService.shutdown();
    }

    private InstructorEntity buildInstructorEntity(String name) {
        return InstructorEntity.instructorBuilder()
                .name(name)
                .build();
    }

    private LectureEntity buildLectureEntity(String title, InstructorEntity instructorEntity) {
        return LectureEntity.builder()
                .title(title)
                .instructorEntity(instructorEntity)
                .build();
    }

    private LectureItemEntity buildLectureItemEntity(LectureEntity lectureEntity, LocalDateTime lectureDateTime, Long capacity) {
        return LectureItemEntity.builder()
                .lectureEntity(lectureEntity)
                .lectureDateTime(lectureDateTime)
                .capacity(capacity)
                .build();
    }

    private LectureItemInventoryEntity buildLectureItemInventoryEntity(LectureItemEntity lectureItemEntity, Long leftSeat) {
        return LectureItemInventoryEntity.builder()
                .lectureItemEntity(lectureItemEntity)
                .leftSeat(leftSeat)
                .build();
    }

    private UserEntity buildUserEntity(String name) {
        return UserEntity.builder()
                .name(name)
                .build();
    }

    private UserLectureEntityId buildUserLectureId(UserEntity userEntity, LectureItemEntity lectureItemEntity) {
        return UserLectureEntityId.builder()
                .userEntityId(userEntity.getId())
                .lectureItemEntityId(lectureItemEntity.getId())
                .build();
    }
}
