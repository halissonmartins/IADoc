package com.halisson.iadoc_library.enums;

import lombok.Getter;

@Getter
public enum EnumQuestionStatus {
	
	//TODO Use primitive int 
	CREATED(1, "Imported"),
	PROCESSING_STARTED(2, "Processing started"),
	SUCCESSFULLY_ANSWERED(3, "Successfully answered"),
	ERRO(4, "Erro");
	
	private Integer code;
	private String description;
	
	private EnumQuestionStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
	
}
