package com.halisson.iadoc_document.batch;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.transaction.PlatformTransactionManager;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.repository.DocumentRepository;
import com.halisson.iadoc_document.storage.IStorageService;

@ExtendWith(MockitoExtension.class)
class DocumentBatchConfigurationTest {

    @Mock
    private JobExplorer jobs;

    @Mock
    private VectorStore vectorStore;

    @Mock
    private PdfDocumentReaderConfig pdfConfig;

    @Mock
    private IStorageService storageService;

    @InjectMocks
    private DocumentBatchConfiguration config;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private JobCompletionNotificationListener listener;

    @Test
    void readerProcessingStartedTest() {
        RepositoryItemReader<Document> reader = config.readerProcessingStarted(documentRepository);
        assertNotNull(reader);
    }

    @Test
    void readerImportedTest() {
        RepositoryItemReader<Document> reader = config.readerImported(documentRepository);
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
        RepositoryItemWriter<Document> writer = config.writer(documentRepository);
        assertNotNull(writer);
    }

    @Test
    void step1Test() {
        Step step = config.step1(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(DocumentInitializeItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void step2Test() {
        Step step = config.step2(jobRepository, transactionManager, mock(RepositoryItemReader.class),
                mock(DocumentItemProcessor.class), mock(RepositoryItemWriter.class));
        assertNotNull(step);
    }

    @Test
    void jobTest() {
        Step step1 = mock(Step.class);
        Step step2 = mock(Step.class);

        Job job = config.importUserJob(jobRepository, step1, step2, listener);
        assertNotNull(job);
    }

    @Test
    void destroyTest() throws Exception {
        when(jobs.getJobNames()).thenReturn(List.of("DocumentProcessorJob"));
        when(jobs.getJobInstanceCount("DocumentProcessorJob")).thenReturn(1L);
        JobInstance instance = new JobInstance(1L, "DocumentProcessorJob");
        when(jobs.getJobInstances("DocumentProcessorJob", 0, 1)).thenReturn(List.of(instance));

        config.destroy();
        
        verify(jobs, times(1)).getJobNames();
    }
}
