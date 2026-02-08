package com.halisson.iadoc_application.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.entity.DocumentStatus;
import com.halisson.iadoc_application.service.DocumentService;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocumentService documentService;

    @Mock
    private PagedResourcesAssembler<DocumentDto> pagedResourcesAssembler;

    @InjectMocks
    private DocumentController documentController;

    private Document document;
    private DocumentDto documentDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        document = new Document();
        document.setId(1L);
        document.setName("test-document");
        document.setDocumentType("pdf");
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.IMPORTED));
        document.setCreatedAt(LocalDateTime.now());

        documentDto = new DocumentDto(document);
    }

    @Test
    void testFindAll_ShouldReturnPagedDocuments() throws Exception {
        var page = new PageImpl<>(List.of(documentDto), PageRequest.of(0, 10), 1);
        when(documentService.findAll(anyInt(), anyInt())).thenReturn(page);
        when(pagedResourcesAssembler.toModel(any())).thenReturn(PagedModel.empty());

        mockMvc.perform(get("/documents")
                .param("pageNumber", "0")
                .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindById_ShouldReturnDocument() throws Exception {
        when(documentService.findById(anyLong())).thenReturn(documentDto);

        mockMvc.perform(get("/documents/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAll_WithDefaultParameters() throws Exception {
        var page = new PageImpl<>(List.of(documentDto));
        when(documentService.findAll(0, 10)).thenReturn(page);
        when(pagedResourcesAssembler.toModel(any())).thenReturn(PagedModel.empty());

        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk());
    }
}
