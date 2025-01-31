package com.halisson.iadoc_application.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.excection.AlreadyExistsException;
import com.halisson.iadoc_application.excection.NotFoundException;
import com.halisson.iadoc_application.repository.DocumentRepository;
import com.halisson.iadoc_application.storage.IStorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService {

	private final IStorageService storageService;
	
	private final DocumentRepository documentRepository;
		
	public DocumentService(
			@Qualifier("minioStorageService") IStorageService storageService, 
			DocumentRepository documentRepository) {
		super();
		this.storageService = storageService;
		this.documentRepository = documentRepository;
	}

	@Transactional
	public void saveAndStore(MultipartFile file) {
		save(file);
		storageService.store(file);
	}

	public void save(MultipartFile file) {
		log.info("OriginalFilename: {}", file.getOriginalFilename());
		log.info("Name: {}", file.getName());
		var document = new Document(file);
		
		var optionalDocument = documentRepository.findByName(document.getName());
		if(!optionalDocument.isEmpty()) {
			throw new AlreadyExistsException("Document already exists with the given name.");
		}
		
		documentRepository.save(document);
	}
	
	@Transactional
	public void saveAndStoreBatch(MultipartFile[] files) {
		Arrays.stream(files).forEach(this::save);
		storageService.storeBatch(files);
	}
	
	public Page<DocumentDto> findAll(Integer pageNumber, Integer pageSize) {
		
		Sort sort = Sort.by("name").ascending();
		Pageable sortPageable = PageRequest.of(pageNumber, pageSize, sort);
		
		return documentRepository.findAll(sortPageable).map(DocumentDto::new);
	}
	
	public DocumentDto findById(Long id) {
		return new DocumentDto(documentRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Document not found with the given ID.")));
	}
	
}
