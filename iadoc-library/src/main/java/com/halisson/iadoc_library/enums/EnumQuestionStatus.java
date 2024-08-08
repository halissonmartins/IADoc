package com.halisson.iadoc_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumQuestionStatus {
	
	CREATED(1, "Imported"),
	PROCESSING_STARTED(2, "Processing started"),
	SUCCESSFULLY_ANSWERED(3, "Successfully answered"),
	ERRO(4, "Erro");
	
	private Integer code;
	private String description;

}
