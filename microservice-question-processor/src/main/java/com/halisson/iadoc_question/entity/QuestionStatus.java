package com.halisson.iadoc_question.entity;

import java.io.Serializable;

import com.halisson.iadoc_question.enums.EnumQuestionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "question", name = "question_status")
public class QuestionStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963490804250279555L;
	
	@Id
	private Integer code;
	
	private String description;
	
	public QuestionStatus(EnumQuestionStatus enumQuestionStatus) {
		this.code = enumQuestionStatus.getCode();
	}

}
