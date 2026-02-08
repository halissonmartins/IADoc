package com.halisson.iadoc_application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.entity.DocumentStatus;
import com.halisson.iadoc_application.entity.Question;
import com.halisson.iadoc_application.entity.QuestionStatus;
import com.halisson.iadoc_application.excection.NotFoundException;
import com.halisson.iadoc_application.repository.QuestionRepository;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private QuestionService questionService;

    private Question question;
    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));
        document.setCreatedAt(LocalDateTime.now());

        question = new Question();
        question.setId(1L);
        question.setQuestion("What is Spring Boot?");
        question.setResponse("Spring Boot is a framework");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        question.setDocument(document);
        question.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testFindAll_Success() {
        var page = new PageImpl<>(List.of(question), PageRequest.of(0, 10), 1);
        when(questionRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = questionService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(questionRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Success() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        QuestionDto result = questionService.findById(1L);

        assertNotNull(result);
        assertEquals("What is Spring Boot?", result.getQuestion());
        verify(questionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound_ShouldThrowException() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> questionService.findById(1L));
    }

    @Test
    void testCreateQuestion_WithDocument_Success() {
        DocumentDto documentDto = new DocumentDto(document);
        when(documentService.findById(1L)).thenReturn(documentDto);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        QuestionDto result = questionService.createQuestion("What is Java?", 1L);

        assertNotNull(result);
        verify(documentService, times(1)).findById(1L);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_WithoutDocument_Success() {
        Question questionWithoutDoc = new Question();
        questionWithoutDoc.setId(2L);
        questionWithoutDoc.setQuestion("What is Java?");
        questionWithoutDoc.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        questionWithoutDoc.setCreatedAt(LocalDateTime.now());

        when(questionRepository.save(any(Question.class))).thenReturn(questionWithoutDoc);

        QuestionDto result = questionService.createQuestion("What is Java?", null);

        assertNotNull(result);
        assertNull(result.getDocumentName());
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_WithZeroDocumentId_Success() {
        Question questionWithoutDoc = new Question();
        questionWithoutDoc.setId(2L);
        questionWithoutDoc.setQuestion("What is Java?");
        questionWithoutDoc.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        questionWithoutDoc.setCreatedAt(LocalDateTime.now());

        when(questionRepository.save(any(Question.class))).thenReturn(questionWithoutDoc);

        QuestionDto result = questionService.createQuestion("What is Java?", 0L);

        assertNotNull(result);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_DocumentNotFound_ShouldThrowException() {
        when(documentService.findById(1L)).thenThrow(new NotFoundException("Document not found"));

        assertThrows(NotFoundException.class, 
            () -> questionService.createQuestion("What is Java?", 1L));
    }
}
