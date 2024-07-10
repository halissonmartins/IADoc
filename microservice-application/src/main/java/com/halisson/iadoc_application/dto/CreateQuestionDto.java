package com.halisson.iadoc_application.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateQuestionDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2325198103922067893L;

	@NotBlank(message = "A questão é obrigatória.")
	private String question;

	private Long documentId;

}
