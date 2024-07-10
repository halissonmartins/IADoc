package com.halisson.iadoc_application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.halisson.iadoc_application.entity.Question;

import lombok.Data;

@Data
public class QuestionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7405574158133504595L;
	
	private Long id;
	
	private String question;

	private String response;

	private String questionStatus;

	private String documentName;

	private LocalDateTime createdAt;

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
