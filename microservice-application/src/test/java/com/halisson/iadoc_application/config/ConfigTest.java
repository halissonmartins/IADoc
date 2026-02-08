package com.halisson.iadoc_application.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.minio.MinioClient;
import io.swagger.v3.oas.models.OpenAPI;

class ConfigTest {

    @Test
    void testMinioConfig_CreatesMinioClient() {
        MinioConfig config = new MinioConfig();
        ReflectionTestUtils.setField(config, "accessKey", "test-access-key");
        ReflectionTestUtils.setField(config, "secretKey", "test-secret-key");
        ReflectionTestUtils.setField(config, "minioUrl", "http://localhost:9000");

        MinioClient client = config.minioClient();

        assertNotNull(client);
    }

    @Test
    void testSwaggerConfig_CreatesOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();

        OpenAPI openAPI = config.openAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertNotNull(openAPI.getInfo().getTitle());
        assertNotNull(openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getVersion());
    }
}
