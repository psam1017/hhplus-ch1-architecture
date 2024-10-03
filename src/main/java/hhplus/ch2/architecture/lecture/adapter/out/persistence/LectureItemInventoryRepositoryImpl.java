package hhplus.ch2.architecture.lecture.adapter.out.persistence;

import hhplus.ch2.architecture.lecture.adapter.out.persistence.entity.LectureItemInventoryEntity;
import hhplus.ch2.architecture.lecture.adapter.out.persistence.jpa.LectureItemInventoryJpaRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemInventoryRepository;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Repository
public class LectureItemInventoryRepositoryImpl implements LectureItemInventoryRepository {

    private final LectureItemInventoryJpaRepository lectureItemInventoryJpaRepository;

    @Override
    public Optional<LectureItemInventory> findByLectureItem(LectureItem lectureItem) {
        return lectureItemInventoryJpaRepository.findByLectureItemId(lectureItem.getId()).map(LectureItemInventoryEntity::toDomain);
    }

    @Override
    public LectureItemInventory decreaseLeftSeatById(LectureItemInventory lectureItemInventory) {
        lectureItemInventoryJpaRepository.decreaseLeftSeatById(lectureItemInventory.getId());
        return LectureItemInventory.builder()
                .id(lectureItemInventory.getId())
                .lectureItem(lectureItemInventory.getLectureItem())
                .leftSeat(lectureItemInventory.getLeftSeat() - 1)
                .build();
    }

    @Override
    public Map<Long, Long> findLeftSeatByLectureItems(List<Long> lectureItemIds) {
        List<LectureItemInventoryEntity> lectureItemInventoryEntities = lectureItemInventoryJpaRepository.findAllByLectureItemIds(lectureItemIds);
        return lectureItemInventoryEntities.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getLectureItemEntity().getId(), LectureItemInventoryEntity::getLeftSeat,
                        (a, b) -> b
                ));
    }
}
