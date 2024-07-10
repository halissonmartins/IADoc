package com.halisson.iadoc_document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halisson.iadoc_document.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	
	//List<Document> findByDocumentStatusCode(Integer documentStatusCode);
	Page<Document> findByDocumentStatusCode(Integer documentStatusCode, Pageable pageable);

}
