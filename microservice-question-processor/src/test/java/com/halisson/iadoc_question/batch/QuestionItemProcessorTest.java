package com.halisson.iadoc_question.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;
import com.halisson.iadoc_question.entity.Document;
import com.halisson.iadoc_question.entity.DocumentStatus;
import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.entity.QuestionStatus;
import com.halisson.iadoc_question.service.ChatService;
import com.halisson.iadoc_question.service.ChatServiceWithoutDocument;

@ExtendWith(MockitoExtension.class)
class QuestionItemProcessorTest {

    @Mock
    private ChatService chatService;

    @Mock
    private ChatServiceWithoutDocument chatServiceWithoutDocument;

    private QuestionItemProcessor processor;
    private Question question;

    @BeforeEach
    void setUp() {
        processor = new QuestionItemProcessor(chatService, chatServiceWithoutDocument);

        Document document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentStatus(new DocumentStatus(
        		EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode(), 
        		EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getDescription()));

        question = new Question();
        question.setId(1L);
        question.setQuestion("What is Spring Boot?");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.PROCESSING_STARTED));
        question.setDocument(document);
        question.setCreatedAt(LocalDateTime.now());
        question.setProcessingStartedAt(LocalDateTime.now());
    }

    @Test
    void testProcess_WithDocument_Success() throws Exception {
        when(chatService.answerQuestion(anyString())).thenReturn("Spring Boot is a framework");

        Question result = processor.process(question);

        assertNotNull(result);
        assertEquals("Spring Boot is a framework", result.getResponse());
        assertEquals(EnumQuestionStatus.SUCCESSFULLY_ANSWERED.getCode(), 
            result.getQuestionStatus().getCode());
        assertNotNull(result.getAnsweredAt());
        verify(chatService, times(1)).answerQuestion(anyString());
    }

    @Test
    void testProcess_WithoutDocument_Success() throws Exception {
        question.setDocument(null);
        when(chatServiceWithoutDocument.answerQuestion(anyString()))
            .thenReturn("Spring Boot is a Java framework");

        Question result = processor.process(question);

        assertNotNull(result);
        assertEquals("Spring Boot is a Java framework", result.getResponse());
        assertEquals(EnumQuestionStatus.SUCCESSFULLY_ANSWERED.getCode(), 
            result.getQuestionStatus().getCode());
        assertNotNull(result.getAnsweredAt());
        verify(chatServiceWithoutDocument, times(1)).answerQuestion(anyString());
    }

    @Test
    void testProcess_UpdatesAnsweredAt() throws Exception {
        LocalDateTime beforeProcessing = LocalDateTime.now();
        when(chatService.answerQuestion(anyString())).thenReturn("Answer");

        Question result = processor.process(question);

        assertNotNull(result.getAnsweredAt());
        assert(result.getAnsweredAt().isAfter(beforeProcessing) || 
               result.getAnsweredAt().isEqual(beforeProcessing));
    }
}
