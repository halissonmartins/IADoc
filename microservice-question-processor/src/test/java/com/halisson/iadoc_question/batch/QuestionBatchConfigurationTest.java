package com.halisson.iadoc_question.batch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.transaction.PlatformTransactionManager;

import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.repository.QuestionRepository;
import com.halisson.iadoc_question.service.ChatService;
import com.halisson.iadoc_question.service.ChatServiceWithoutDocument;

@ExtendWith(MockitoExtension.class)
class QuestionBatchConfigurationTest {

    @Mock
    private JobExplorer jobs;

    @Mock
    private ChatService chatService;

    @Mock
    private ChatServiceWithoutDocument chatServiceWithoutDocument;

    @InjectMocks
    private QuestionBatchConfiguration config;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private JobCompletionNotificationListener listener;

    @Test
    void readerProcessingStartedQuestionWithOutDocumentTest() {
        RepositoryItemReader<Question> reader = config
                .readerProcessingStartedQuestionWithOutDocument(questionRepository);
        assertNotNull(reader);
    }

    @Test
    void readerImportedQuestionWithOutDocumentTest() {
        RepositoryItemReader<Question> reader = config.readerImportedQuestionWithOutDocument(questionRepository);
        assertNotNull(reader);
    }

    @Test
    void readerProcessingStartedQuestionWithDocumentTest() {
        RepositoryItemReader<Question> reader = config.readerProcessingStartedQuestionWithDocument(questionRepository);
        assertNotNull(reader);
    }

    @Test
    void readerImportedQuestionWithDocumentTest() {
        RepositoryItemReader<Question> reader = config.readerImportedQuestionWithDocument(questionRepository);
        assertNotNull(reader);
    }

    @Test
    void initializerDocumentTest() {
        assertNotNull(config.initializerDocument());
    }

    @Test
    void processorDocumentTest() {
        assertNotNull(config.processorDocument());
    }

    @Test
    void writerTest() {
        RepositoryItemWriter<Question> writer = config.writer(questionRepository);
        assertNotNull(writer);
    }

    @Test
    void step1Test() {
        Step step = config.step1(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(QuestionInitializeItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void step2Test() {
        Step step = config.step2(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(QuestionItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void step3Test() {
        Step step = config.step3(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(QuestionInitializeItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void step4Test() {
        Step step = config.step4(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(QuestionItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void jobTest() {
        Step step1 = mock(Step.class);
        Step step2 = mock(Step.class);
        Step step3 = mock(Step.class);
        Step step4 = mock(Step.class);

        Job job = config.importUserJob(jobRepository, step1, step2, step3, step4, listener);
        assertNotNull(job);
    }

    @Test
    void destroyTest() throws Exception {
        when(jobs.getJobNames()).thenReturn(List.of("QuestionProcessorJob"));
        when(jobs.getJobInstanceCount("QuestionProcessorJob")).thenReturn(1L);
        JobInstance instance = new JobInstance(1L, "QuestionProcessorJob");
        when(jobs.getJobInstances("QuestionProcessorJob", 0, 1)).thenReturn(List.of(instance));

        config.destroy();
        
        verify(jobs, times(1)).getJobNames();
    }
}
