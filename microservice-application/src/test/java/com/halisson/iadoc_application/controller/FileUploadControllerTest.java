package com.halisson.iadoc_application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.halisson.iadoc_application.service.DocumentService;

@ExtendWith(MockitoExtension.class)
class FileUploadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController).build();
    }

    @Test
    void testHandleFilesUpload_ShouldAcceptFiles() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        doNothing().when(documentService).saveAndStoreBatch(any());

        mockMvc.perform(multipart("/upload").file(file))
                .andExpect(status().isAccepted());

        verify(documentService, times(1)).saveAndStoreBatch(any());
    }

    @Test
    void testHandleFilesUpload_WithMultipleFiles() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "test1.pdf",
                "application/pdf",
                "test content 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "test2.pdf",
                "application/pdf",
                "test content 2".getBytes()
        );

        doNothing().when(documentService).saveAndStoreBatch(any());

        mockMvc.perform(multipart("/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isAccepted());

        verify(documentService, times(1)).saveAndStoreBatch(any());
    }
}
