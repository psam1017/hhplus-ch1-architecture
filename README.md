# 테이블 설계 설명

(항해플러스 과제) 이 문서는 각 테이블의 설계 이유와 목적을 설명합니다.

---

## 1. users 테이블

**구조:**

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT COMMENT '사용자 식별키',
    dtype VARCHAR(31) COMMENT '사용자 유형. NULL 인 경우 기본 사용자, INSTRUCTOR 인 경우 강사.',
    name VARCHAR(255) COMMENT '사용자 이름',
    PRIMARY KEY (id)
);
```

**설계 이유:**

- **id**: 모든 사용자를 고유하게 식별하기 위한 기본 키입니다.
- **dtype**: 사용자 유형을 지정하기 위한 컬럼으로, `NULL`이면 일반 사용자, `'INSTRUCTOR'`이면 강사를 의미합니다. 이를 통해 한 테이블에서 다양한 사용자 유형을 관리할 수 있습니다.
- **name**: 사용자의 이름을 저장하는 컬럼입니다.

**비고:**

- 사용자 유형은 `dtype` 컬럼 하나로 관리하며 애플리케이션의 타입 상속을 위해 단일 테이블 전략을 적용했습니다.

---

## 2. lectures 테이블

**구조:**

```sql
CREATE TABLE lectures (
    id BIGINT AUTO_INCREMENT COMMENT '특강 식별키',
    instructor_id BIGINT COMMENT '강사 식별키 (users 테이블의 id)',
    title VARCHAR(255) COMMENT '특강 제목',
    PRIMARY KEY (id),
    CONSTRAINT fk_lectures_instructor_id
        FOREIGN KEY (instructor_id) REFERENCES users (id)
);
```

**설계 이유:**

- **id**: 각 특강을 고유하게 식별하기 위한 기본 키입니다.
- **instructor_id**: 특강을 진행하는 강사의 `users` 테이블 `id`를 참조하는 외래 키입니다. 애플리케이션에서는 JPA 에서 InstructorEntity 라는 타입만을 허용하여 여러 사용자 유형 중에서도 강사의 키값만 넣을 수 있도록 하였습니다.
- **title**: 특강의 제목을 저장합니다.

---

## 3. lecture_items 테이블

**구조:**

```sql
CREATE TABLE lecture_items (
    id BIGINT AUTO_INCREMENT COMMENT '특강 일정 식별키',
    capacity BIGINT COMMENT '수용 가능 인원',
    lecture_date_time DATETIME COMMENT '특강 날짜 및 시간',
    lecture_id BIGINT COMMENT '특강 식별키 (lectures 테이블의 id)',
    PRIMARY KEY (id),
    CONSTRAINT fk_lecture_items_lecture_id
        FOREIGN KEY (lecture_id) REFERENCES lectures (id)
);
```

**설계 이유:**

- **id**: 각 특강 일정을 고유하게 식별하기 위한 기본 키입니다.
- **capacity**: 해당 일정의 최대 수용 인원을 지정합니다.
- **lecture_date_time**: 특강이 진행되는 날짜와 시간을 저장합니다.
- **lecture_id**: 이 일정이 속한 특강 정보를 나타내며, `lectures` 테이블의 `id`를 참조합니다.

**비고:**

- 하나의 특강이 여러 일정으로 구성될 수 있으므로, 일정 정보를 별도로 관리하도록 정규화했습니다.

---

## 4. lecture_item_inventories 테이블

**구조:**

```sql
CREATE TABLE lecture_item_inventories (
    id BIGINT AUTO_INCREMENT COMMENT '특강 일정별 빈 자리 식별키. 수강신청을 제어하기 위한 LOCK 의 범위를 최소화합니다.',
    lecture_item_id BIGINT UNIQUE COMMENT '특강 일정 식별키 (lecture_items 테이블의 id)',
    left_seat BIGINT COMMENT '남은 좌석 수. 각 레코드에 LOCK 을 걸고, 이 컬럼의 수를 비교하여 신청이 가능한지 여부를 판단합니다.',
    PRIMARY KEY (id),
    CONSTRAINT fk_lecture_item_inventories_lecture_item_id
        FOREIGN KEY (lecture_item_id) REFERENCES lecture_items (id)
);
```

**설계 이유:**

- **id**: 각 재고 관리를 위한 고유 식별자입니다.
- **lecture_item_id**: 해당 재고가 어떤 특강 일정에 속하는지 나타내며, `lecture_items`의 `id`를 참조합니다. `UNIQUE` 제약 조건으로 1:1 관계를 유지합니다.
- **left_seat**: 남은 좌석 수를 관리합니다.

**비고:**

- 신청 가능 여부를 검증하기 위해 leftSeat 컬럼의 숫자를 조회하여 비교(leftSeat > 0)하며, 이때 락을 걸어 동시성 제어를 수행합니다.
- 락을 거는 테이블과 그렇지 않은 테이블을 분리함으로써 락이 걸린 동안에도 다른 테이블은 수정이 가능하도록 의도하였습니다.

---

## 5. user_lectures 테이블

**구조:**

```sql
CREATE TABLE user_lectures (
    lecture_id BIGINT NOT NULL COMMENT '복합키-신청한 특강 일정(lecture_items 테이블의 id)',
    user_id BIGINT NOT NULL COMMENT '복합키-수강하는 사용자(users 테이블의 id)',
    created_date_time DATETIME COMMENT '특강 신청 일시',
    PRIMARY KEY (lecture_id, user_id),
    CONSTRAINT fk_user_lectures_lecture_id
        FOREIGN KEY (lecture_id) REFERENCES lecture_items (id),
    CONSTRAINT fk_user_lectures_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
);
```

**설계 이유:**

- **lecture_id** 및 **user_id**: 복합 기본 키로 설정하여 한 사용자가 동일한 특강 일정을 중복 신청하는 것을 방지합니다.
- **created_date_time**: 수강 신청이 이루어진 시점을 기록합니다.

**비고:**

- 사용자와 특강 일정 간의 N:M 관계를 관리하는 조인 테이블입니다.

---

# 결론

- 사용자와 강사를 단일 테이블 전략을 사용하면서 정규화하여 하나의 테이블을 생성했습니다.
- 특강과 특강 일정 테이블을 생성하고, 특강 신청 API 를 호출할 때, LOCK 을 걸어 동시성 제어를 수행하기 위한 특강 잔여좌석 테이블을 생성했습니다.
- 사용자가 신청한 특강 테이블을 생성하여 중복 신청을 방지하도록 의도했습니다.
