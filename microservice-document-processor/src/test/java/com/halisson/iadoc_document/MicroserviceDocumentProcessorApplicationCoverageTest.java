package com.halisson.iadoc_document;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class MicroserviceDocumentProcessorApplicationCoverageTest {

    @Test
    void main() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication
                    .when(() -> SpringApplication.run(MicroserviceDocumentProcessorApplication.class, new String[] {}))
                    .thenReturn(null);

            MicroserviceDocumentProcessorApplication.main(new String[] {});

            springApplication.verify(
                    () -> SpringApplication.run(MicroserviceDocumentProcessorApplication.class, new String[] {}));
        }
    }
}
