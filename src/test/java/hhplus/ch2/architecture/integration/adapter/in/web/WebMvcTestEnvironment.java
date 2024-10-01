package hhplus.ch2.architecture.integration.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.ch2.architecture.lecture.adapter.in.web.LectureController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                LectureController.class
        }
)
public abstract class WebMvcTestEnvironment {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    protected String createJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
