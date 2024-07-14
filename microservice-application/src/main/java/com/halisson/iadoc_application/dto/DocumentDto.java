package com.halisson.iadoc_application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.halisson.iadoc_application.entity.Document;

import lombok.Data;

@Data
public class DocumentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -316948171195445375L;
	
	private Long id;
	
	private String name;

	private String documentType;

	private String documentStatus;

	private LocalDateTime createdAt;
	
	private LocalDateTime processedAt;

	public DocumentDto(Document document) {
		super();
		this.id = document.getId();
		this.name = document.getName();
		this.documentType = document.getDocumentType();
		this.documentStatus = document.getDocumentStatus() != null ? document.getDocumentStatus().getDescription() : null;
		this.createdAt = document.getCreatedAt();
		this.processedAt = document.getProcessedAt();
	}
	
	

}
