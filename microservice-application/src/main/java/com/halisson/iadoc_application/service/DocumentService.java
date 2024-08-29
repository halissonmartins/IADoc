package com.halisson.iadoc_application.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.dto.DocumentDto;
import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.excection.AlreadyExistsException;
import com.halisson.iadoc_application.excection.NotFoundException;
import com.halisson.iadoc_application.repository.DocumentRepository;
import com.halisson.iadoc_application.storage.StorageService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

	private final StorageService storageService;
	private final DocumentRepository documentRepository;
	
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
	
	public List<DocumentDto> findAll() {
		return documentRepository.findAll().stream().map(DocumentDto::new).collect(Collectors.toList());
	}
	
	public DocumentDto findById(Long id) {
		return new DocumentDto(documentRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Document not found with the given ID.")));
	}
	
}
