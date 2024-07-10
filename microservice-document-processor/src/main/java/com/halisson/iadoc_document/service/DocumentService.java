package com.halisson.iadoc_document.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.enums.EnumDocumentStatus;
import com.halisson.iadoc_document.repository.DocumentRepository;

@Service
public class DocumentService {

	private final DocumentRepository documentRepository;

	public DocumentService(
			DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}
	
	/*public List<Document> findPendings() {
		return documentRepository.findByDocumentStatusCode(EnumDocumentStatus.IMPORTED.getCode());
	}
	
	public Document getById(Long id) {
		return documentRepository.findById(id).get();
	}
	
	public void save(Document document) {
		documentRepository.save(document);
	}*/
	
}
