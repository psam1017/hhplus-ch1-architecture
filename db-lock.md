### DB Lock

#### 1. Lock 을 걸지 않은 경우 발생하는 문제
```
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
```

![1 no-lock](https://github.com/user-attachments/assets/5624e7c6-6b9b-4946-acd7-3f3953e5fd8e)

동시에 여러 사용자가 동일한 리소스(강의 빈 좌석)에 접근할 때, 락을 걸지 않으면 동일한 좌석을 초과하는 신청이 발생할 수 있습니다.

위의 테스트에서, 좌석이 30석밖에 남지 않은 상황에서 40명의 사용자가 동시에 신청하면 동시성 문제로 인해 좌석 수를 초과하는 신청이 성공할 수 있습니다.

이러한 문제는 다음과 같은 이유로 발생합니다:
- **트랜잭션 격리 수준**이 충분하지 않으면 다른 트랜잭션이 읽고 쓰는 데이터를 동시에 접근하게 되며, 이를 통해 예상치 못한 데이터 수정이 발생할 수 있습니다.
- **초과 처리**: 한 트랜잭션이 좌석 수를 업데이트하는 동안 다른 트랜잭션이 같은 좌석을 참조하고 업데이트를 시도하는 경우, 실제로는 좌석이 없는 상황에서도 추가 신청이 허용됩니다.

#### 2. 락의 종류
1. **낙관적 락 (Optimistic Lock)**:
    - 트랜잭션 동안 데이터를 수정하는 경우가 드물다고 가정하여 락을 최소화합니다. 트랜잭션이 완료될 때까지 다른 사용자는 데이터를 읽을 수 있으며, 수정 시 충돌이 발생하면 이를 해결합니다.
    - 장점: 성능 최적화 가능, 트랜잭션 충돌이 적은 상황에 적합
    - 단점: 충돌이 발생하면 재시도나 충돌 해결을 위한 비용이 증가

2. **비관적 락 (Pessimistic Lock)**:
    - 데이터를 읽는 순간 락을 걸어 다른 트랜잭션이 해당 데이터에 접근하지 못하게 합니다. 트랜잭션이 종료될 때까지 락을 유지하며, 읽기와 쓰기를 모두 제한하는 방식입니다.
    - 장점: 트랜잭션 충돌 방지, 안전한 데이터 처리
    - 단점: 락이 걸린 동안 다른 트랜잭션이 대기해야 하므로 성능 저하 가능

#### 3. JPA 비관적 락의 쓰기 잠금과 읽기 잠금
- **쓰기 잠금(PESSIMISTIC_WRITE)**:
    - 엔티티를 조회할 때 SELECT ... FOR UPDATE 쿼리를 사용한 배타 락을 건다.
    - 잠금을 건 레코드는 해당 트랜잭션이 커밋을 해야 접근할 수 있다.

- **읽기 잠금(PESSIMISTIC_READ)**:
    - 엔티티를 조회할 때 SELECT ... FOR SHARE 쿼리를 수행
    - 데이터를 반복 읽기만 하고 수정하지 않는 용도로 락을 걸 때 사용한다.
    - 잠금을 건 레코드에 대해 조회가 가능하지만 데이터를 수정할 때 트랜잭션 충돌이 발생한다.

#### 4. 비관적 락과 쓰기 잠금을 사용하여 동시성 제어
```
@Lock(LockModeType.PESSIMISTIC_WRITE)
@QueryHints({
        @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")
})
@Query("""
        SELECT lii
        FROM LectureItemInventoryEntity lii
        WHERE lii.lectureItemEntity.id = :lectureItemId
        """)
Optional<LectureItemInventoryEntity> findByLectureItemId(Long lectureItemId);
```

![2 pessimistic-write-lock](https://github.com/user-attachments/assets/0a6b8dda-7fe5-48e6-9d08-efc02be54670)

- **원리**: 좌석 수를 읽고 이를 업데이트하는 순간 다른 트랜잭션이 접근하지 못하도록 DB 레벨에서 락을 걸어, 동시 접근을 차단합니다. 이를 통해 서로 다른 트랜잭션이 데이터 무결성을 위반하지 않도록 방지합니다.

#### 5. 결론
비관적 락과 배타락을 사용하면 트랜잭션 충돌과 데이터 불일치 문제를 해결할 수 있습니다. 이 방식은 성능에 영향을 미칠 수 있지만, 정확성과 무결성이 보장되어야 하는 환경에서 필요한 방법입니다.

#### 6. (추가) QueryHints 로 타임아웃 설정
`@QueryHints`의 `javax.persistence.lock.timeout`을 5000ms로 설정한 것은 락을 대기하는 시간에 제한을 두기 위함입니다.

이 값은 트랜잭션이 일정 시간 동안 락을 획득하지 못하면 타임아웃을 발생시켜, 불필요한 대기 상태를 방지하고 시스템의 성능을 유지하는 데 도움을 줍니다.
