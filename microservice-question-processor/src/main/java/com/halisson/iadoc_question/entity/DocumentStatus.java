package com.halisson.iadoc_question.entity;

import java.io.Serializable;

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
@Table(schema = "question", name = "document_status")
public class DocumentStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963490804250279555L;

	@Id
	private Integer code;
	
	private String description;

}
