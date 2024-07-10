package com.halisson.iadoc_application.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.entity.Document;
import com.halisson.iadoc_application.repository.DocumentRepository;
import com.halisson.iadoc_application.storage.StorageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService {

	private final StorageService storageService;
	private final DocumentRepository documentRepository;

	public DocumentService(
			StorageService storageService,
			DocumentRepository documentRepository) {
		this.storageService = storageService;
		this.documentRepository = documentRepository;
	}
	
	public void saveAndStore(MultipartFile file) {
		save(file);
		storageService.store(file);
	}

	public void save(MultipartFile file) {
		log.info("OriginalFilename: {}", file.getOriginalFilename());
		log.info("Name: {}", file.getName());
		documentRepository.save(new Document(file));
	}
	
	public void saveAndStoreBatch(MultipartFile[] files) {
		Arrays.stream(files).forEach(this::save);
		storageService.storeBatch(files);
	}
	
	public List<String> listAllNames(){
		return documentRepository.findAll().stream().map(Document::getName).collect(Collectors.toList());
	}
	
}
