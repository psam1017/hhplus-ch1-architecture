package hhplus.ch2.architecture.lecture.application;

import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.RegisterLectureUseCase;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemInventoryRepository;
import hhplus.ch2.architecture.lecture.application.port.out.LectureItemRepository;
import hhplus.ch2.architecture.lecture.application.port.out.UserLectureRepository;
import hhplus.ch2.architecture.lecture.application.port.out.UserRepository;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureItemException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchLectureItemInventoryException;
import hhplus.ch2.architecture.lecture.common.exception.NoSuchUserException;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItem;
import hhplus.ch2.architecture.lecture.domain.entity.LectureItemInventory;
import hhplus.ch2.architecture.lecture.domain.entity.User;
import hhplus.ch2.architecture.lecture.domain.entity.UserLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RegisterLectureService implements RegisterLectureUseCase {

    private final UserRepository userRepository;
    private final LectureItemRepository lectureItemRepository;
    private final LectureItemInventoryRepository lectureItemInventoryRepository;
    private final UserLectureRepository userLectureRepository;

    @Override
    public LectureRegistrationResult registerLecture(RegisterLectureCommand command) {
        Long lectureItemId = command.lectureItemId();
        Long userId = command.userId();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(userId));
        LectureItem lectureItem = lectureItemRepository.findById(lectureItemId).orElseThrow(() -> new NoSuchLectureItemException(lectureItemId));
        LectureItemInventory lectureItemInventory = lectureItemInventoryRepository.findByLectureItem(lectureItem).orElseThrow(() -> new NoSuchLectureItemInventoryException(lectureItemId));

        UserLecture userLecture = lectureItem.registerUser(lectureItemInventory.getLeftSeat(), user);
        lectureItemInventory = lectureItemInventoryRepository.decreaseLeftSeatById(lectureItemInventory);

        // TODO: 2024-09-29 STEP 4 과제
//        if (userLectureRepository.existsByUserAndLecture(user, lecture)) {
//            throw new IllegalArgumentException("Already registered");
//        }
        userLectureRepository.save(userLecture);
        return new LectureRegistrationResult(lectureItemId, userId);
    }
}
