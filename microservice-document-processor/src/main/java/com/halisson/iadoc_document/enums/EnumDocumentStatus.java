package com.halisson.iadoc_document.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDocumentStatus {
	
	IMPORTED(1, "Imported"),
	PROCESSING_STARTED(2, "Processing started"),
	SUCCESSFULLY_PROCESSED(3, "Successfully processed"),
	ERRO(4, "Erro");
	
	private Integer code;
	private String description;
}
