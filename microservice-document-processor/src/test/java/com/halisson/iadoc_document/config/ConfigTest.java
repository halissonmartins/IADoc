package com.halisson.iadoc_document.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.test.util.ReflectionTestUtils;

import io.minio.MinioClient;

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
    void testPdfConfig_CreatesPdfDocumentReaderConfig() {
        PdfConfig config = new PdfConfig();

        PdfDocumentReaderConfig pdfConfig = config.pdfDocumentReaderConfig();

        assertNotNull(pdfConfig);
    }
}
