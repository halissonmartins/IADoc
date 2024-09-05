package com.halisson.iadoc_application.controller.interfaces;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.halisson.iadoc_application.dto.DocumentDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
					description = "List of documents returned successfully.",
					content = {@Content(array = @ArraySchema(schema = @Schema(implementation = DocumentDto.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad request occurred."),
			@ApiResponse(responseCode = "500", description = "Internal error occurred."),
	})
	ResponseEntity<PagedModel<EntityModel<DocumentDto>>> findAll(
			@Parameter(description = "Number of page requested. The default value is 0.", example = "1") Integer pageNumber, 
			@Parameter(description = "Size of page requested. The default value is 10", example = "5") Integer pageSize);

	@Operation(summary = "Get a document by ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Document returned successfully.",
					content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DocumentDto.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request occurred."),
			@ApiResponse(responseCode = "404", description = "Documento ID not found."),
			@ApiResponse(responseCode = "500", description = "Internal error occurred."),
	})
	ResponseEntity<DocumentDto> findById(@Parameter(description = "ID of document to be searched") Long id);

}