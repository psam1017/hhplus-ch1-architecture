
# MySQL 버전입니다.

CREATE DATABASE hhplus_ch2 CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE hhplus_ch2;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT COMMENT '사용자 식별키',
    dtype VARCHAR(31) COMMENT '사용자 유형. NULL 인 경우 기본 사용자, INSTRUCTOR 인 경우 강사.',
    name VARCHAR(255) COMMENT '사용자 이름',
    PRIMARY KEY (id)
);

CREATE TABLE lectures (
    id BIGINT AUTO_INCREMENT COMMENT '특강 식별키',
    instructor_id BIGINT COMMENT '강사 식별키 (users 테이블의 id)',
    title VARCHAR(255) COMMENT '특강 제목',
    PRIMARY KEY (id),
    CONSTRAINT fk_lectures_instructor_id
        FOREIGN KEY (instructor_id) REFERENCES users (id)
);

CREATE TABLE lecture_items (
    id BIGINT AUTO_INCREMENT COMMENT '특강 일정 식별키',
    capacity BIGINT COMMENT '수용 가능 인원',
    lecture_date_time DATETIME COMMENT '특강 날짜 및 시간',
    lecture_id BIGINT COMMENT '특강 식별키 (lectures 테이블의 id)',
    PRIMARY KEY (id),
    CONSTRAINT fk_lecture_items_lecture_id
        FOREIGN KEY (lecture_id) REFERENCES lectures (id)
);

CREATE TABLE lecture_item_inventories (
    id BIGINT AUTO_INCREMENT COMMENT '특강 일정별 빈 자리 식별키. 수강신청을 제어하기 위한 LOCK 의 범위를 최소화합니다.',
    lecture_item_id BIGINT UNIQUE COMMENT '특강 일정 식별키 (lecture_items 테이블의 id)',
    left_seat BIGINT COMMENT '남은 좌석 수. 각 레코드에 LOCK 을 걸고, 이 컬럼의 수를 비교하여 신청이 가능한지 여부를 판단합니다.',
    PRIMARY KEY (id),
    CONSTRAINT fk_lecture_item_inventories_lecture_item_id
        FOREIGN KEY (lecture_item_id) REFERENCES lecture_items (id)
);

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
