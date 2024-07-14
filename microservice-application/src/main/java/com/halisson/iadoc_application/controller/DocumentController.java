package com.halisson.iadoc_application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.service.DocumentService;

@RestController
@RequestMapping("documents")
public class DocumentController {

	private final DocumentService documentService;

	public DocumentController(DocumentService documentService) {
		super();
		this.documentService = documentService;
	}
	

	@GetMapping
	public List<DocumentDto> findAll() {
		return documentService.findAll();
	}
	
	@GetMapping("/{id}")
	public DocumentDto findById(@PathVariable Long id) {
		return documentService.findById(id);
	}
}
