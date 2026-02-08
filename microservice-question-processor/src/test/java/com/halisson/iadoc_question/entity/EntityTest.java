package com.halisson.iadoc_question.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;

class EntityTest {

    @Test
    void testQuestionSettersAndGetters() {
        Question question = new Question();
        QuestionStatus status = new QuestionStatus(EnumQuestionStatus.CREATED);
        Document document = new Document(1L);
        LocalDateTime now = LocalDateTime.now();

        question.setId(1L);
        question.setQuestion("What is Java?");
        question.setResponse("Java is a programming language");
        question.setQuestionStatus(status);
        question.setDocument(document);
        question.setCreatedAt(now);
        question.setProcessingStartedAt(now);
        question.setAnsweredAt(now);

        assertEquals(1L, question.getId());
        assertEquals("What is Java?", question.getQuestion());
        assertEquals("Java is a programming language", question.getResponse());
        assertEquals(status, question.getQuestionStatus());
        assertEquals(document, question.getDocument());
        assertEquals(now, question.getCreatedAt());
        assertEquals(now, question.getProcessingStartedAt());
        assertEquals(now, question.getAnsweredAt());
    }

    @Test
    void testDocumentSettersAndGetters() {
        Document document = new Document();
        DocumentStatus status = new DocumentStatus();
        status.setCode(EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode());

        document.setId(1L);
        document.setName("test-document");
        document.setDocumentStatus(status);

        assertEquals(1L, document.getId());
        assertEquals("test-document", document.getName());
        assertEquals(status, document.getDocumentStatus());
    }

    @Test
    void testDocumentConstructor_WithId() {
        Document document = new Document(1L);

        assertEquals(1L, document.getId());
    }

    @Test
    void testQuestionStatusConstructor_WithEnum() {
        QuestionStatus status = new QuestionStatus(EnumQuestionStatus.SUCCESSFULLY_ANSWERED);

        assertEquals(EnumQuestionStatus.SUCCESSFULLY_ANSWERED.getCode(), status.getCode());
    }

    @Test
    void testQuestionStatusSettersAndGetters() {
        QuestionStatus status = new QuestionStatus();
        status.setCode(1);
        status.setDescription("Created");

        assertEquals(1, status.getCode());
        assertEquals("Created", status.getDescription());
    }

    @Test
    void testDocumentStatusSettersAndGetters() {
        DocumentStatus status = new DocumentStatus();
        status.setCode(1);
        status.setDescription("Imported");

        assertEquals(1, status.getCode());
        assertEquals("Imported", status.getDescription());
    }

    @Test
    void testQuestionAllArgsConstructor() {
        QuestionStatus status = new QuestionStatus(EnumQuestionStatus.CREATED);
        Document document = new Document(1L);
        LocalDateTime now = LocalDateTime.now();

        Question question = new Question(
            1L,
            "What is Spring?",
            "Spring is a framework",
            status,
            document,
            now,
            now,
            now
        );

        assertNotNull(question);
        assertEquals(1L, question.getId());
        assertEquals("What is Spring?", question.getQuestion());
        assertEquals("Spring is a framework", question.getResponse());
    }

    @Test
    void testDocumentAllArgsConstructor() {
        DocumentStatus status = new DocumentStatus();
        status.setCode(1);

        Document document = new Document(1L, "test-doc", status);

        assertNotNull(document);
        assertEquals(1L, document.getId());
        assertEquals("test-doc", document.getName());
    }

    @Test
    void testQuestionNoArgsConstructor() {
        Question question = new Question();
        assertNotNull(question);
    }

    @Test
    void testDocumentNoArgsConstructor() {
        Document document = new Document();
        assertNotNull(document);
    }

    @Test
    void testQuestionStatusNoArgsConstructor() {
        QuestionStatus status = new QuestionStatus();
        assertNotNull(status);
    }

    @Test
    void testDocumentStatusNoArgsConstructor() {
        DocumentStatus status = new DocumentStatus();
        assertNotNull(status);
    }

    @Test
    void testQuestionStatusAllArgsConstructor() {
        QuestionStatus status = new QuestionStatus(1, "Created");

        assertEquals(1, status.getCode());
        assertEquals("Created", status.getDescription());
    }

    @Test
    void testDocumentStatusAllArgsConstructor() {
        DocumentStatus status = new DocumentStatus(1, "Imported");

        assertEquals(1, status.getCode());
        assertEquals("Imported", status.getDescription());
    }
}
