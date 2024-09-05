package com.halisson.iadoc_application.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.controller.interfaces.IDocumentController;
import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.service.DocumentService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("documents")
public class DocumentController implements IDocumentController {

	private final DocumentService documentService;
	private final PagedResourcesAssembler<DocumentDto> pagedResourcesAssembler;
	
	@Override
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<DocumentDto>>> findAll(
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		
		Page<DocumentDto> response = documentService.findAll(pageNumber, pageSize);
		response.map(r -> r.add(linkTo(methodOn(this.getClass()).findById(r.getId())).withSelfRel()));
		
		return ResponseEntity.ok(pagedResourcesAssembler.toModel(response));
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<DocumentDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(
				documentService.findById(id).add( 
						linkTo(methodOn(this.getClass()).findAll(null, null)).withRel("documents") ));
	}
}
