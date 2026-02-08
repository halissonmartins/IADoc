package com.halisson.iadoc_question.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halisson.iadoc_library.enums.EnumQuestionStatus;
import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.entity.QuestionStatus;

class QuestionInitializeItemProcessorTest {

    private QuestionInitializeItemProcessor processor;
    private Question question;

    @BeforeEach
    void setUp() {
        processor = new QuestionInitializeItemProcessor();

        question = new Question();
        question.setId(1L);
        question.setQuestion("What is Java?");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        question.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testProcess_ShouldUpdateStatusToProcessingStarted() throws Exception {
        Question result = processor.process(question);

        assertNotNull(result);
        assertEquals(EnumQuestionStatus.PROCESSING_STARTED.getCode(), 
            result.getQuestionStatus().getCode());
        assertNotNull(result.getProcessingStartedAt());
    }

    @Test
    void testProcess_ShouldSetProcessingStartedAt() throws Exception {
        LocalDateTime beforeProcessing = LocalDateTime.now();
        
        Question result = processor.process(question);

        assertNotNull(result.getProcessingStartedAt());
        assert(result.getProcessingStartedAt().isAfter(beforeProcessing) || 
               result.getProcessingStartedAt().isEqual(beforeProcessing));
    }

    @Test
    void testProcess_ShouldNotModifyOtherFields() throws Exception {
        Long originalId = question.getId();
        String originalQuestion = question.getQuestion();
        LocalDateTime originalCreatedAt = question.getCreatedAt();

        Question result = processor.process(question);

        assertEquals(originalId, result.getId());
        assertEquals(originalQuestion, result.getQuestion());
        assertEquals(originalCreatedAt, result.getCreatedAt());
    }
}
