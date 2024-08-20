package com.halisson.iadoc_application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.controller.interfaces.IDocumentController;
import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.service.DocumentService;

@RestController
@RequestMapping("documents")
public class DocumentController implements IDocumentController {

	private final DocumentService documentService;

	public DocumentController(DocumentService documentService) {
		super();
		this.documentService = documentService;
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<DocumentDto>> findAll() {
		return ResponseEntity.ok(documentService.findAll());
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<DocumentDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(documentService.findById(id));
	}
}
