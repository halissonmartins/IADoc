package com.halisson.iadoc_application.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;

class EntityTest {

    @Test
    void testDocumentConstructor_FromMultipartFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-document.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        Document document = new Document(file);

        assertEquals("test-document", document.getName());
        assertEquals("pdf", document.getDocumentType());
        assertNotNull(document.getDocumentStatus());
        assertEquals(EnumDocumentStatus.IMPORTED.getCode(), document.getDocumentStatus().getCode());
    }

    @Test
    void testDocumentConstructor_WithId() {
        Document document = new Document(1L);

        assertEquals(1L, document.getId());
    }

    @Test
    void testDocumentStatus_WithEnum() {
        DocumentStatus status = new DocumentStatus(EnumDocumentStatus.SUCCESSFULLY_PROCESSED);

        assertEquals(EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode(), status.getCode());
    }

    @Test
    void testQuestionConstructor_WithQuestionAndDocumentId() {
        Question question = new Question("What is Java?", 1L);

        assertEquals("What is Java?", question.getQuestion());
        assertNotNull(question.getDocument());
        assertEquals(1L, question.getDocument().getId());
        assertNotNull(question.getQuestionStatus());
        assertEquals(EnumQuestionStatus.CREATED.getCode(), question.getQuestionStatus().getCode());
    }

    @Test
    void testQuestionConstructor_WithoutDocumentId() {
        Question question = new Question("What is Java?", null);

        assertEquals("What is Java?", question.getQuestion());
        assertEquals(null, question.getDocument());
        assertNotNull(question.getQuestionStatus());
    }

    @Test
    void testQuestionStatus_WithEnum() {
        QuestionStatus status = new QuestionStatus(EnumQuestionStatus.SUCCESSFULLY_ANSWERED);

        assertEquals(EnumQuestionStatus.SUCCESSFULLY_ANSWERED.getCode(), status.getCode());
    }

    @Test
    void testDocumentSettersAndGetters() {
        Document document = new Document();
        document.setId(1L);
        document.setName("test");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));
        document.setCreatedAt(LocalDateTime.now());
        document.setProcessedAt(LocalDateTime.now());

        assertEquals(1L, document.getId());
        assertEquals("test", document.getName());
        assertEquals("pdf", document.getDocumentType());
        assertNotNull(document.getDocumentStatus());
        assertNotNull(document.getCreatedAt());
        assertNotNull(document.getProcessedAt());
    }

    @Test
    void testQuestionSettersAndGetters() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestion("What is Spring?");
        question.setResponse("Spring is a framework");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        question.setDocument(new Document(1L));
        question.setCreatedAt(LocalDateTime.now());
        question.setAnsweredAt(LocalDateTime.now());

        assertEquals(1L, question.getId());
        assertEquals("What is Spring?", question.getQuestion());
        assertEquals("Spring is a framework", question.getResponse());
        assertNotNull(question.getQuestionStatus());
        assertNotNull(question.getDocument());
        assertNotNull(question.getCreatedAt());
        assertNotNull(question.getAnsweredAt());
    }
}
