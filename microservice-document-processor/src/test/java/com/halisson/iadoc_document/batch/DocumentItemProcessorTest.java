package com.halisson.iadoc_document.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_document.storage.IStorageService;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

@ExtendWith(MockitoExtension.class)
class DocumentItemProcessorTest {

    @Mock
    private VectorStore vectorStore;

    @Mock
    private IStorageService storageService;

    private DocumentItemProcessor processor;
    private com.halisson.iadoc_document.entity.Document document;

    @BeforeEach
    void setUp() {
    	
    	PdfDocumentReaderConfig pdfConfig = PdfDocumentReaderConfig.defaultConfig();
    	
        processor = new DocumentItemProcessor(vectorStore, pdfConfig, storageService);

        document = new com.halisson.iadoc_document.entity.Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.PROCESSING_STARTED));
        document.setCreatedAt(LocalDateTime.now());
        document.setProcessingStartedAt(LocalDateTime.now());
    }

    @Test
    void testProcess_Success() throws Exception {

        byte[] pdfContent = new ClassPathResource("ua000182.pdf").getContentAsByteArray();
        
        ByteArrayResource resource = new ByteArrayResource(pdfContent);
        when(storageService.loadAsResource(any())).thenReturn(resource);

        com.halisson.iadoc_document.entity.Document result = processor.process(document);

        assertNotNull(result);
        assertEquals(EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode(), result.getDocumentStatus().getCode());
        assertNotNull(result.getProcessedAt());
        verify(vectorStore, times(1)).accept(any(List.class));
    }

    @Test
    void testProcess_UpdatesStatus() throws Exception {

        byte[] pdfContent = new ClassPathResource("ua000182.pdf").getContentAsByteArray();
        
        ByteArrayResource resource = new ByteArrayResource(pdfContent);
        when(storageService.loadAsResource("test-document.pdf")).thenReturn(resource);

        com.halisson.iadoc_document.entity.Document result = processor.process(document);

        assertEquals(EnumDocumentStatus.SUCCESSFULLY_PROCESSED.getCode(), result.getDocumentStatus().getCode());
        assertNotNull(result.getProcessedAt());
    }
}
