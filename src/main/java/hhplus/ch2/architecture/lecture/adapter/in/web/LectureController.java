package hhplus.ch2.architecture.lecture.adapter.in.web;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.common.ApiResponse;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.request.LectureRegistrationForm;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.request.LectureSearchCond;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.response.LectureApiResponse;
import hhplus.ch2.architecture.lecture.adapter.in.web.model.response.LectureRegistrationApiResult;
import hhplus.ch2.architecture.lecture.application.command.FindAvailableLectureCommand;
import hhplus.ch2.architecture.lecture.application.command.RegisterLectureCommand;
import hhplus.ch2.architecture.lecture.application.port.in.FindAvailableLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.in.FindMyLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.in.RegisterLectureUseCase;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LectureController {

    private final RegisterLectureUseCase registerLectureUseCase;
    private final FindAvailableLecturesUseCase findAvailableLecturesUseCase;
    private final FindMyLecturesUseCase findMyLecturesUseCase;

    /**
     * 특강 신청 API
     * - 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
     * - 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있습니다.
     * - 특강은 선착순 30명만 신청 가능합니다.
     * - 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다.
     */
    @PostMapping("/api/lectures/items/{lectureItemId}/register")
    public ApiResponse<LectureRegistrationApiResult> registerForLecture(
            @PathVariable Long lectureItemId,
            @RequestBody @Valid LectureRegistrationForm form
    ) {
        RegisterLectureCommand command = new RegisterLectureCommand(
                form.userId(),
                lectureItemId
        );

        LectureRegistrationResult result = registerLectureUseCase.registerLecture(command);
        LectureRegistrationApiResult apiResult = new LectureRegistrationApiResult(
                result.lectureItemId(),
                result.userId()
        );

        return ApiResponse.ok(apiResult);
    }

    /**
     * - 날짜별로 현재 신청 가능한 특강 목록을 조회하는 API 를 작성합니다.
     * - 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기전 목록을 조회해볼 수 있어야 합니다.
     */
    @GetMapping("/api/lectures/items")
    public ApiResponse<List<LectureApiResponse>> findAvailableLectureItems(
            @ModelAttribute LectureSearchCond cond
    ) {
        FindAvailableLectureCommand command = new FindAvailableLectureCommand(
                cond.lectureDate()
        );

        List<LectureApiResponse> apiResult = findAvailableLecturesUseCase.findAvailableLectureItems(command)
                .stream()
                .map(lectureResponse -> new LectureApiResponse(
                        lectureResponse.lectureId(),
                        lectureResponse.lectureTitle(),
                        lectureResponse.instructorName(),
                        lectureResponse.lectureDateTime(),
                        lectureResponse.leftSeat(),
                        lectureResponse.capacity()
                ))
                .toList();

        return ApiResponse.ok(apiResult);
    }

    /**
     * - 특정 userId 로 신청 완료된 특강 목록을 조회하는 API 를 작성합니다.
     * - 각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 합니다.
     */
    @GetMapping("/api/users/{userId}/lectures")
    public ApiResponse<List<LectureApiResponse>> findLecturesByUser(
            @PathVariable Long userId
    ) {
        List<LectureApiResponse> apiResult = findMyLecturesUseCase.findMyLectures(userId)
                .stream()
                .map(lectureResponse -> new LectureApiResponse(
                        lectureResponse.lectureId(),
                        lectureResponse.lectureTitle(),
                        lectureResponse.instructorName(),
                        lectureResponse.lectureDateTime(),
                        lectureResponse.leftSeat(),
                        lectureResponse.capacity()
                ))
                .toList();

        return ApiResponse.ok(apiResult);
    }
}
