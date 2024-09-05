package com.halisson.iadoc_application.controller.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.dto.QuestionDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Question", description = "Endpoint for list questions that have been saved.")
public interface IQuestionController {
	
	@Operation(summary = "Get all questions saved.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
				description = "List of questions returned successfully.",
				content = {@Content(array = @ArraySchema(schema = @Schema(implementation = DocumentDto.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad request occurred."),
			@ApiResponse(responseCode = "500", description = "Internal error occurred."),
	})
	ResponseEntity<Page<QuestionDto>> findAll(
			@Parameter(description = "Number of page requested. The default value is 0.", example = "1") Integer pageNumber, 
			@Parameter(description = "Size of page requested. The default value is 10", example = "5") Integer pageSize);

	@Operation(summary = "Get a question by ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Question returned successfully.",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request occurred."),
			@ApiResponse(responseCode = "404", description = "Question ID not found."),
			@ApiResponse(responseCode = "500", description = "Internal error occurred."),
	})
	ResponseEntity<QuestionDto> findById(@Parameter(description = "ID of question to be searched") Long id);

	@Operation(summary = "Create a new question.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", 
				description = "Question created successfully.",
				content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request occurred."),
			@ApiResponse(responseCode = "404", description = "Document ID not found."),
			@ApiResponse(responseCode = "500", description = "Internal error occurred."),
	})
	ResponseEntity<QuestionDto> create(CreateQuestionDto dto);

}