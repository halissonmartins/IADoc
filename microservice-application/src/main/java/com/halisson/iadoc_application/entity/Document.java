package com.halisson.iadoc_application.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.enums.EnumDocumentStatus;

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
@Table(schema = "application", name = "documents")
public class Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1925562525212473382L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String documentType;
	
	@ManyToOne
	@JoinColumn(name="document_status_code", nullable=false)
	private DocumentStatus documentStatus;
	
	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private LocalDateTime processedAt;
	
	public Document(MultipartFile file) {
	  
	  String[] valuesArray = file.getOriginalFilename().split("\\.");
	  
	  this.name = valuesArray[0];
	  this.documentType = valuesArray[1];
	  this.documentStatus = new DocumentStatus(EnumDocumentStatus.IMPORTED);
	}

	public Document(Long id) {
		super();
		this.id = id;
	}

}
