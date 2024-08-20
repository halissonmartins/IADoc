package com.halisson.iadoc_application.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Question creation properties", name = "Question Creation")
public class CreateQuestionDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2325198103922067893L;

	@Schema(description = "Text with the question", example = "What is the smallest country in the world?")
	@NotBlank(message = "The question is required.")
	private String question;

	@Schema(description = "ID of document", example = "1", requiredMode = RequiredMode.NOT_REQUIRED)
	private Long documentId;

}
