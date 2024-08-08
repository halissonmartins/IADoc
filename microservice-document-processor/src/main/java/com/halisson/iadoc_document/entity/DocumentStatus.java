package com.halisson.iadoc_document.entity;

import java.io.Serializable;

import com.halisson.iadoc_library.enums.EnumDocumentStatus;

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
@Table(schema = "document", name = "document_status")
public class DocumentStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963490804250279555L;
	
	public DocumentStatus(EnumDocumentStatus enumDocumentStatus) {
		super();
		this.code = enumDocumentStatus.getCode();
	}

	@Id
	private Integer code;
	
	private String description;

}
