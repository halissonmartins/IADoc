package com.halisson.iadoc_document.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

class DocumentInitializeItemProcessorTest {

    private DocumentInitializeItemProcessor processor;
    private Document document;

    @BeforeEach
    void setUp() {
        processor = new DocumentInitializeItemProcessor();

        document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));
        document.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testProcess_ShouldUpdateStatusToProcessingStarted() throws Exception {
        Document result = processor.process(document);

        assertNotNull(result);
        assertEquals(EnumDocumentStatus.PROCESSING_STARTED.getCode(), 
            result.getDocumentStatus().getCode());
        assertNotNull(result.getProcessingStartedAt());
    }

    @Test
    void testProcess_ShouldSetProcessingStartedAt() throws Exception {
        LocalDateTime beforeProcessing = LocalDateTime.now();
        
        Document result = processor.process(document);

        assertNotNull(result.getProcessingStartedAt());
        assert(result.getProcessingStartedAt().isAfter(beforeProcessing) || 
               result.getProcessingStartedAt().isEqual(beforeProcessing));
    }

    @Test
    void testProcess_ShouldNotModifyOtherFields() throws Exception {
        Long originalId = document.getId();
        String originalName = document.getName();
        String originalType = document.getDocumentType();
        LocalDateTime originalCreatedAt = document.getCreatedAt();

        Document result = processor.process(document);

        assertEquals(originalId, result.getId());
        assertEquals(originalName, result.getName());
        assertEquals(originalType, result.getDocumentType());
        assertEquals(originalCreatedAt, result.getCreatedAt());
    }
}
