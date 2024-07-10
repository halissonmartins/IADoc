package com.halisson.iadoc_question.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "question", name = "questions")
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1925562525212473382L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String question;
	
	private String response;
	
	@ManyToOne
	@JoinColumn(name="question_status_code", nullable=false)
	private QuestionStatus questionStatus;
	
	@ManyToOne
	@JoinColumn(name = "document_id")
	private Document document;
	
	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private LocalDateTime processingStartedAt;
	
	private LocalDateTime answeredAt;
	
}
