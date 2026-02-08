package com.halisson.iadoc_application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.entity.DocumentStatus;
import com.halisson.iadoc_application.entity.Question;
import com.halisson.iadoc_application.entity.QuestionStatus;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;

class DtoTest {

    @Test
    void testDocumentDto_FromEntity() {
        Document document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED.getCode(), EnumDocumentStatus.IMPORTED.getDescription()));
        document.setCreatedAt(LocalDateTime.now());
        document.setProcessedAt(LocalDateTime.now());

        DocumentDto dto = new DocumentDto(document);

        assertEquals(1L, dto.getId());
        assertEquals("test-document", dto.getName());
        assertEquals("pdf", dto.getDocumentType());
        assertNotNull(dto.getDocumentStatus());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getProcessedAt());
    }

    @Test
    void testDocumentDto_WithNullStatus() {
        Document document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(null);
        document.setCreatedAt(LocalDateTime.now());

        DocumentDto dto = new DocumentDto(document);

        assertNull(dto.getDocumentStatus());
    }

    @Test
    void testQuestionDto_FromEntity_WithDocument() {
        Document document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");

        Question question = new Question();
        question.setId(1L);
        question.setQuestion("What is Spring Boot?");
        question.setResponse("Framework");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED.getCode(), EnumQuestionStatus.CREATED.getDescription()));
        question.setDocument(document);
        question.setCreatedAt(LocalDateTime.now());
        question.setAnsweredAt(LocalDateTime.now());

        QuestionDto dto = new QuestionDto(question);

        assertEquals(1L, dto.getId());
        assertEquals("What is Spring Boot?", dto.getQuestion());
        assertEquals("Framework", dto.getResponse());
        assertNotNull(dto.getQuestionStatus());
        assertEquals("test-document", dto.getDocumentName());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getAnsweredAt());
    }

    @Test
    void testQuestionDto_FromEntity_WithoutDocument() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestion("What is Java?");
        question.setResponse("Language");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        question.setDocument(null);
        question.setCreatedAt(LocalDateTime.now());

        QuestionDto dto = new QuestionDto(question);

        assertEquals(1L, dto.getId());
        assertEquals("What is Java?", dto.getQuestion());
        assertNull(dto.getDocumentName());
    }

    @Test
    void testCreateQuestionDto_Setters() {
        CreateQuestionDto dto = new CreateQuestionDto();
        dto.setQuestion("Test question");
        dto.setDocumentId(1L);

        assertEquals("Test question", dto.getQuestion());
        assertEquals(1L, dto.getDocumentId());
    }

    @Test
    void testCreateQuestionDto_WithNullDocumentId() {
        CreateQuestionDto dto = new CreateQuestionDto();
        dto.setQuestion("Test question");
        dto.setDocumentId(null);

        assertEquals("Test question", dto.getQuestion());
        assertNull(dto.getDocumentId());
    }
}
