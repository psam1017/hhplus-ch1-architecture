package hhplus.ch2.architecture.integration.adapter.in.web;

import hhplus.ch2.architecture.lecture.adapter.in.web.model.request.LectureRegistrationForm;
import hhplus.ch2.architecture.lecture.application.port.in.FindAvailableLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.in.FindMyLecturesUseCase;
import hhplus.ch2.architecture.lecture.application.port.in.RegisterLectureUseCase;
import hhplus.ch2.architecture.lecture.application.response.LectureRegistrationResult;
import hhplus.ch2.architecture.lecture.application.response.LectureResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LectureControllerTest extends WebMvcTestEnvironment {

    @MockBean
    RegisterLectureUseCase registerLectureUseCase;

    @MockBean
    FindAvailableLecturesUseCase findAvailableLecturesUseCase;

    @MockBean
    FindMyLecturesUseCase findMyLecturesUseCase;

    @DisplayName("강의를 신청할 수 있다.")
    @Test
    void registerForLecture() throws Exception {
        // mock
        when(registerLectureUseCase.registerLecture(any()))
                .thenReturn(new LectureRegistrationResult(1L, 1L));

        // given
        Long lectureId = 1L;
        LectureRegistrationForm form = new LectureRegistrationForm(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/lectures/{lectureId}/register", lectureId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(form))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.lectureId").value(1L));
    }

    @DisplayName("수강할 수 있는 모든 강의를 찾을 수 있다.")
    @Test
    void findAvailableLectures() throws Exception {
        // mock
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        given(findAvailableLecturesUseCase.findAvailableLectures(any()))
                .willReturn(List.of(
                        new LectureResponse(1L, "강의1", "강사1", thisSaturday, 0L),
                        new LectureResponse(2L, "강의2", "강사2", thisSaturday.plusWeeks(1), 0L)
                ));

        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/lectures")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].lectureId").value(1L))
                .andExpect(jsonPath("$.data[0].lectureTitle").value("강의1"))
                .andExpect(jsonPath("$.data[0].instructorName").value("강사1"))
                .andExpect(jsonPath("$.data[0].lectureDateTime").value(thisSaturday.format(formatter)))
                .andExpect(jsonPath("$.data[0].registeredCount").value(0L))
                .andExpect(jsonPath("$.data[0].capacity").value(30L))
                .andExpect(jsonPath("$.data[1].lectureId").value(2L))
                .andExpect(jsonPath("$.data[1].lectureTitle").value("강의2"))
                .andExpect(jsonPath("$.data[1].instructorName").value("강사2"))
                .andExpect(jsonPath("$.data[1].lectureDateTime").value(thisSaturday.plusWeeks(1).format(formatter)))
                .andExpect(jsonPath("$.data[1].registeredCount").value(0L))
                .andExpect(jsonPath("$.data[1].capacity").value(30L));
    }

    @DisplayName("내가 신청한 강의를 찾을 수 있다.")
    @Test
    void findLecturesByUser() throws Exception {
        // mock
        LocalDateTime thisSaturday = LocalDateTime.now().with(DayOfWeek.SATURDAY);

        given(findMyLecturesUseCase.findMyLectures(any()))
                .willReturn(List.of(
                        new LectureResponse(1L, "강의1", "강사1", thisSaturday, 0L)
                ));

        // given
        Long userId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{userId}/lectures", userId)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].lectureId").value(1L))
                .andExpect(jsonPath("$.data[0].lectureTitle").value("강의1"))
                .andExpect(jsonPath("$.data[0].instructorName").value("강사1"))
                .andExpect(jsonPath("$.data[0].lectureDateTime").value(thisSaturday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andExpect(jsonPath("$.data[0].registeredCount").value(0L))
                .andExpect(jsonPath("$.data[0].capacity").value(30L));
    }
}
