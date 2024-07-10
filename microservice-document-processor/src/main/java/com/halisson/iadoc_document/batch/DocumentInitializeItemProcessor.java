package com.halisson.iadoc_document.batch;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_document.enums.EnumDocumentStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentInitializeItemProcessor implements ItemProcessor<Document, Document> {
			
	public DocumentInitializeItemProcessor() {
		super();
	}

	@Override
	public Document process(Document document) throws Exception{
		
		log.debug("Initializing document {}", document.getName());
		
		document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.PROCESSING_STARTED));
		document.setProcessingStartedAt(LocalDateTime.now());
        
        return document;
	}
}
