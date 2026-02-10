package com.halisson.iadoc_application;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class MicroserviceApplicationCoverageTest {

    @Test
    void main() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(MicroserviceApplication.class, new String[] {}))
                    .thenReturn(null);

            MicroserviceApplication.main(new String[] {});

            springApplication.verify(() -> SpringApplication.run(MicroserviceApplication.class, new String[] {}));
        }
    }
}
