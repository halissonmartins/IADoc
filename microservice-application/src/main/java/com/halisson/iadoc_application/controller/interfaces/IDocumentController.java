package com.halisson.iadoc_application.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.halisson.iadoc_application.dto.DocumentDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Document", description = "Endpoint for list documents that have been saved.")
public interface IDocumentController {

	@Operation(summary = "Get all documents saved.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "List de documents returned successfully.",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDto.class)) })
	})
	ResponseEntity<List<DocumentDto>> findAll();

	@Operation(summary = "Get a document by ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Document returned successfully.",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDto.class)) })
	})
	ResponseEntity<DocumentDto> findById(@Parameter(description = "ID of document to be searched") Long id);

}