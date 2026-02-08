package com.halisson.iadoc_application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.entity.DocumentStatus;
import com.halisson.iadoc_application.excection.AlreadyExistsException;
import com.halisson.iadoc_application.excection.NotFoundException;
import com.halisson.iadoc_application.repository.DocumentRepository;
import com.halisson.iadoc_application.storage.IStorageService;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private IStorageService storageService;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document document;
    private MockMultipartFile file;

    @BeforeEach
    void setUp() {
        document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));
        document.setCreatedAt(LocalDateTime.now());

        file = new MockMultipartFile(
                "file",
                "test-document.pdf",
                "application/pdf",
                "test content".getBytes()
        );
    }

    @Test
    void testSave_Success() {
        when(documentRepository.findByName(anyString())).thenReturn(List.of());
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        documentService.save(file);

        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testSave_DocumentAlreadyExists_ShouldThrowException() {
        when(documentRepository.findByName(anyString())).thenReturn(List.of(document));

        assertThrows(AlreadyExistsException.class, () -> documentService.save(file));
    }

    @Test
    void testSaveAndStore_Success() {
        when(documentRepository.findByName(anyString())).thenReturn(List.of());
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        doNothing().when(storageService).store(any(MultipartFile.class));

        documentService.saveAndStore(file);

        verify(documentRepository, times(1)).save(any(Document.class));
        verify(storageService, times(1)).store(any(MultipartFile.class));
    }

    @Test
    void testSaveAndStoreBatch_Success() {
        MultipartFile[] files = {file};

        when(documentRepository.findByName(anyString())).thenReturn(List.of());
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        doNothing().when(storageService).storeBatch(any());

        documentService.saveAndStoreBatch(files);

        verify(documentRepository, times(1)).save(any(Document.class));
        verify(storageService, times(1)).storeBatch(any());
    }

    @Test
    void testFindAll_Success() {
        var page = new PageImpl<>(List.of(document), PageRequest.of(0, 10), 1);
        when(documentRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = documentService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(documentRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_Success() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        DocumentDto result = documentService.findById(1L);

        assertNotNull(result);
        assertEquals("test-document", result.getName());
        verify(documentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound_ShouldThrowException() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.findById(1L));
    }
}
