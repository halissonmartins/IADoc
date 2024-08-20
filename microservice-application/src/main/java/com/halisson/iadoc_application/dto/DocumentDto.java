package com.halisson.iadoc_application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.halisson.iadoc_application.entity.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Document properties", name = "Document")
public class DocumentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -316948171195445375L;
	
	@Schema(description = "ID of document", example = "1")
	private Long id;
	
	@Schema(description = "Name of document", example = "Document name")
	private String name;

	@Schema(description = "Type of document", example = "PDF")
	private String documentType;

	@Schema(description = "Status of document", example = "Successfully processed")
	private String documentStatus;

	@Schema(description = "Creation date of document")
	private LocalDateTime createdAt;
	
	@Schema(description = "Processed date of document")
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
