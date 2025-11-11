package com.halisson.iadoc_library.enums;

import lombok.Getter;

@Getter
public enum EnumDocumentStatus {
	
	//TODO Use primitive int
	IMPORTED(1, "Imported"),
	PROCESSING_STARTED(2, "Processing started"),
	SUCCESSFULLY_PROCESSED(3, "Successfully processed"),
	ERRO(4, "Erro");
	
	private Integer code;
	private String description;
	
	private EnumDocumentStatus(Integer code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
