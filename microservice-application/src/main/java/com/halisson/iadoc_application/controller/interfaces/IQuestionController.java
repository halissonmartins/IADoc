package com.halisson.iadoc_application.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.QuestionDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Question", description = "Endpoint for list questions that have been saved.")
public interface IQuestionController {
	
	@Operation(summary = "Get all questions saved.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List de questions returned successfully.")
	})
	ResponseEntity<List<QuestionDto>> findAll();

	@Operation(summary = "Get a question by ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Question returned successfully.")
	})
	ResponseEntity<QuestionDto> findById(@Parameter(description = "ID of question to be searched") Long id);

	@Operation(summary = "Create a new question.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Question created successfully.")
	})
	ResponseEntity<QuestionDto> create(CreateQuestionDto dto);

}