package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureItemJpaRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemRepository;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Repository
public class LectureItemRepositoryImpl implements LectureItemRepository {

    private final LectureItemJpaRepository lectureItemJpaRepository;

    @Override
    public Optional<LectureItem> findById(Long lectureItemId) {
        return lectureItemJpaRepository.findById(lectureItemId).map(LectureItemEntity::toDomain);
    }

    @Override
    public List<LectureItem> findAllAvailableLectureItems() {
        return lectureItemJpaRepository.findAllAvailableLectureItems()
                .stream()
                .map(LectureItemEntity::toDomain)
                .toList();
    }

    @Override
    public List<LectureItem> findAllAvailableLectureItems(LocalDate localDate) {
        return lectureItemJpaRepository.findAllAvailableLectureItems(
                        localDate.atStartOfDay(),
                        localDate.atTime(23, 59, 59)
                )
                .stream()
                .map(LectureItemEntity::toDomain)
                .toList();
    }

    @Override
    public List<LectureItem> findAllByUserId(Long userId) {
        return lectureItemJpaRepository.findAllByUserId(userId)
                .stream()
                .map(LectureItemEntity::toDomain)
                .toList();
    }
}
