package com.halisson.iadoc_application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.entity.DocumentStatus;
import com.halisson.iadoc_application.entity.Question;
import com.halisson.iadoc_application.entity.QuestionStatus;
import com.halisson.iadoc_application.service.QuestionService;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;
import com.halisson.iadoc_library.enums.EnumQuestionStatus;


@WebMvcTest(QuestionController.class) // 1. Ativa o contexto MVC e HATEOAS
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc; // 2. Deixe o Spring injetar o MockMvc configurado

    @MockitoBean
    private QuestionService questionService; // 3. Use @MockBean em vez de @Mock

    private ObjectMapper objectMapper;
    private Question question;
    private QuestionDto questionDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Document document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));

        question = new Question();
        question.setId(1L);
        question.setQuestion("What is Spring Boot?");
        question.setResponse("Spring Boot is a framework");
        question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.CREATED));
        question.setDocument(document);
        question.setCreatedAt(LocalDateTime.now());

        questionDto = new QuestionDto(question);
    }

    @Test
    void testFindAll_ShouldReturnPagedQuestions() throws Exception {
        var page = new PageImpl<>(List.of(questionDto), PageRequest.of(0, 10), 1);
        when(questionService.findAll(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/questions")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_ShouldReturnQuestion() throws Exception {
        when(questionService.findById(anyLong())).thenReturn(questionDto);

        mockMvc.perform(get("/questions/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate_ShouldCreateQuestion() throws Exception {
        CreateQuestionDto createDto = new CreateQuestionDto();
        createDto.setQuestion("What is Java?");
        createDto.setDocumentId(1L);

        when(questionService.createQuestion(anyString(), anyLong())).thenReturn(questionDto);

        mockMvc.perform(post("/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreate_WithoutDocument() throws Exception {
        CreateQuestionDto createDto = new CreateQuestionDto();
        createDto.setQuestion("What is Java?");

        when(questionService.createQuestion(anyString(), any())).thenReturn(questionDto);

        mockMvc.perform(post("/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testFindAll_WithDefaultParameters() throws Exception {
        var page = new PageImpl<>(List.of(questionDto));
        when(questionService.findAll(eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/questions"))
        		.andDo(print())
                .andExpect(status().isOk());
    }
}
