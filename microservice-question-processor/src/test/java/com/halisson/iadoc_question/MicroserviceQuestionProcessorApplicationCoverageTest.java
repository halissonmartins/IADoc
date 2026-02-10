package com.halisson.iadoc_question;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class MicroserviceQuestionProcessorApplicationCoverageTest {

    @Test
    void main() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication
                    .when(() -> SpringApplication.run(MicroserviceQuestionProcessorApplication.class, new String[] {}))
                    .thenReturn(null);

            MicroserviceQuestionProcessorApplication.main(new String[] {});

            springApplication.verify(
                    () -> SpringApplication.run(MicroserviceQuestionProcessorApplication.class, new String[] {}));
        }
    }
}
