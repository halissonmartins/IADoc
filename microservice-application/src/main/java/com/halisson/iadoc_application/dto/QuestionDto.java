package com.halisson.iadoc_application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.halisson.iadoc_application.entity.Question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Question properties", name = "Question")
public class QuestionDto extends RepresentationModel<QuestionDto> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7405574158133504595L;
	
	@Schema(description = "ID of question", example = "1")
	private Long id;
	
	@Schema(description = "Text with the question", example = "What is the smallest country in the world?")
	private String question;

	@Schema(description = "Text with the response", example = "Vatican City")
	private String response;

	@Schema(description = "Status of document", example = "Successfully answered")
	private String questionStatus;

	@Schema(description = "Name of document", example = "Document name")
	private String documentName;

	@Schema(description = "Creation date of question")
	private LocalDateTime createdAt;

	@Schema(description = "Answer date of question")
	private LocalDateTime answeredAt;
	
	public QuestionDto(Question question) {
		this.id = question.getId();
		this.question = question.getQuestion();
		this.response = question.getResponse();
		this.questionStatus = question.getQuestionStatus().getDescription();
		this.documentName = question.getDocument() != null ? question.getDocument().getName() : null;
		this.createdAt = question.getCreatedAt();
		this.answeredAt = question.getAnsweredAt();
	}

}
