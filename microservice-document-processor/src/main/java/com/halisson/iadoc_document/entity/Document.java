package com.halisson.iadoc_document.entity;

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
@Table(schema = "document", name = "documents")
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
	  
	  private LocalDateTime processingStartedAt;
	  
	  private LocalDateTime processedAt;
	  
	  

}
