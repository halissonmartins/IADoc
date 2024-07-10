package com.halisson.iadoc_application.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DocumentDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -316948171195445375L;
	
	private String name;
	private String documentType;

}
