package com.halisson.iadoc_application.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BaseControllerTest {

    private MockMvc mockMvc;
    private BaseController baseController;

    @BeforeEach
    void setUp() {
        baseController = new BaseController();
        mockMvc = MockMvcBuilders.standaloneSetup(baseController).build();
    }

    @Test
    void testShowUploadForm_ShouldReturnUploadFormView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("uploadForm"));
    }
}
