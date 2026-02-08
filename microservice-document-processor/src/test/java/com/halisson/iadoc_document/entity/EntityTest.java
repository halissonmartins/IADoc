package com.halisson.iadoc_document.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.halisson.iadoc_library.enums.EnumDocumentStatus;

class EntityTest {

    @Test
    void testDocumentSettersAndGetters() {
        Document document = new Document();
        DocumentStatus status = new DocumentStatus(EnumDocumentStatus.IMPORTED);
        LocalDateTime now = LocalDateTime.now();

        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(status);
        document.setCreatedAt(now);
        document.setProcessingStartedAt(now);
        document.setProcessedAt(now);

        assertEquals(1L, document.getId());
        assertEquals("test-document", document.getName());
        assertEquals("pdf", document.getDocumentType());
        assertEquals(status, document.getDocumentStatus());
        assertEquals(now, document.getCreatedAt());
        assertEquals(now, document.getProcessingStartedAt());
        assertEquals(now, document.getProcessedAt());
    }

    @Test
    void testDocumentStatusConstructor_WithEnum() {
        DocumentStatus status = new DocumentStatus(EnumDocumentStatus.SUCCESSFULLY_PROCESSED);

        assertEquals(EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode(), status.getCode());
    }

    @Test
    void testDocumentStatusSettersAndGetters() {
        DocumentStatus status = new DocumentStatus();
        status.setCode(1);
        status.setDescription("Test Status");

        assertEquals(1, status.getCode());
        assertEquals("Test Status", status.getDescription());
    }

    @Test
    void testDocumentAllArgsConstructor() {
        DocumentStatus status = new DocumentStatus(EnumDocumentStatus.IMPORTED);
        LocalDateTime now = LocalDateTime.now();

        Document document = new Document(
            1L,
            "test-document",
            "pdf",
            status,
            now,
            now,
            now
        );

        assertNotNull(document);
        assertEquals(1L, document.getId());
        assertEquals("test-document", document.getName());
        assertEquals("pdf", document.getDocumentType());
        assertEquals(status, document.getDocumentStatus());
    }

    @Test
    void testDocumentStatusAllArgsConstructor() {
        DocumentStatus status = new DocumentStatus(1, "Imported");

        assertNotNull(status);
        assertEquals(1, status.getCode());
        assertEquals("Imported", status.getDescription());
    }

    @Test
    void testDocumentNoArgsConstructor() {
        Document document = new Document();
        assertNotNull(document);
    }

    @Test
    void testDocumentStatusNoArgsConstructor() {
        DocumentStatus status = new DocumentStatus();
        assertNotNull(status);
    }
}
