package com.halisson.iadoc_question.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClientRequestSpec requestSpec;

    @Mock
    private CallResponseSpec responseSpec;

    @Mock
    private VectorStore vectorStore;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.build()).thenReturn(chatClient);
        chatService = new ChatService(chatClientBuilder, vectorStore);        

        // MOCK DO RESOURCE: Criamos um recurso em memória com o conteúdo do template
        String templateContent = "Pergunta: {input} Contexto: {documents}";
        Resource mockResource = new ByteArrayResource(templateContent.getBytes());

        // INJEÇÃO MANUAL: O Spring não roda aqui, então injetamos via reflexão
        ReflectionTestUtils.setField(chatService, "sbPromptTemplate", mockResource);
    }

    @Test
    void testAnswerQuestion_WithDocuments_Success() {
        String question = "What is Spring Boot?";
        String expectedAnswer = "Spring Boot is a framework";

        List<Document> documents = Arrays.asList(
            new Document("Spring Boot is a framework for Java"),
            new Document("It simplifies Spring application development")
        );

        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(documents);
        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedAnswer);

        String result = chatService.answerQuestion(question);

        assertNotNull(result);
        assertEquals(expectedAnswer, result);
    }

    @Test
    void testAnswerQuestion_EmptyDocuments_Success() {
        String question = "What is Java?";
        String expectedAnswer = "Java is a programming language";

        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Arrays.asList());
        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedAnswer);

        String result = chatService.answerQuestion(question);

        assertNotNull(result);
        assertEquals(expectedAnswer, result);
    }
}

@ExtendWith(MockitoExtension.class)
class ChatServiceWithoutDocumentTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClientRequestSpec requestSpec;

    @Mock
    private CallResponseSpec responseSpec;

    private ChatServiceWithoutDocument chatService;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.build()).thenReturn(chatClient);
        chatService = new ChatServiceWithoutDocument(chatClientBuilder);

        // MOCK DO RESOURCE: Criamos um recurso em memória com o conteúdo do template
        String templateContent = "Pergunta: {input}";
        Resource mockResource = new ByteArrayResource(templateContent.getBytes());

        // INJEÇÃO MANUAL: O Spring não roda aqui, então injetamos via reflexão
        ReflectionTestUtils.setField(chatService, "sbPromptTemplateWithoutDocument", mockResource);
    }

    @Test
    void testAnswerQuestion_Success() {
        String question = "What is Spring Boot?";
        String expectedAnswer = "Spring Boot is a framework";

        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(expectedAnswer);

        String result = chatService.answerQuestion(question);

        assertNotNull(result);
        assertEquals(expectedAnswer, result);
    }

    @Test
    void testAnswerQuestion_DifferentQuestions() {
        String question1 = "Explain Java";
        String answer1 = "Java is object-oriented";

        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(answer1);

        String result = chatService.answerQuestion(question1);

        assertEquals(answer1, result);
    }
}

@ExtendWith(MockitoExtension.class)
class AbstractChatServiceTest {

    @Mock
    private ChatClient chatClient;

    private AbstractChatService abstractChatService;

    @BeforeEach
    void setUp() {
        abstractChatService = new AbstractChatService(chatClient) {
            @Override
            public String answerQuestion(String message) {
                return "Test answer";
            }
        };
    }

    @Test
    void testAnswerQuestion_ReturnsExpectedValue() {
        String result = abstractChatService.answerQuestion("Test question");
        
        assertEquals("Test answer", result);
    }
}
