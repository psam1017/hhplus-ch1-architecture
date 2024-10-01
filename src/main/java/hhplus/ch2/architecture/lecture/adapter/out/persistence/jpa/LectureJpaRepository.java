package hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureEntity;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

    @Query("""
            select new hhplus.ch2.architecture.lecture.application.response.LectureResponse(
                l.id,
                l.title,
                l.instructorName,
                l.lectureDateTime,
                count(ul)
            )
            from LectureEntity l
            left join UserLectureEntity ul on l.id = ul.lecture.id
            group by l.id
            having count(ul) < :capacity
            """)
    List<LectureResponse> findAllByRegisteredCountLessThan(long capacity);

    @Query("""
            select new hhplus.ch2.architecture.lecture.application.response.LectureResponse(
                l.id,
                l.title,
                l.instructorName,
                l.lectureDateTime,
                count(ul)
            )
            from LectureEntity l
            left join UserLectureEntity ul on l.id = ul.lecture.id
            where l.lectureDateTime between :startDateTime and :endDateTime
            group by l.id
            having count(ul) < :capacity
            """)
    List<LectureResponse> findAllByLectureDateTimeBetweenAndRegisteredCountLessThan(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            long capacity
    );

    @Query("""
            select new hhplus.ch2.architecture.lecture.application.response.LectureResponse(
                l.id,
                l.title,
                l.instructorName,
                l.lectureDateTime,
                count(ul)
            )
            from LectureEntity l
            join UserLectureEntity ul on l.id = ul.lecture.id
            where ul.user.id = :userId
            group by l.id
            """)
    List<LectureResponse> findAllByUserId(Long userId);
}
