package com.halisson.iadoc_document.batch;

import java.time.LocalDateTime;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.batch.item.ItemProcessor;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_document.storage.IStorageService;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DocumentItemProcessor implements ItemProcessor<Document, Document> {
	
	private final VectorStore vectorStore;
	private final PdfDocumentReaderConfig pdfConfig;
	private final IStorageService storageService;

	@Override
	public Document process(Document document) throws Exception{
		
		log.debug("Processing document {}", document.getName());
		
		var documentName = document.getName()+"."+document.getDocumentType();		
				
		var pdfReader = new PagePdfDocumentReader(storageService.loadAsResource(documentName), pdfConfig);
		var textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        
        log.debug("Finished processing document {}", documentName);
        
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.SUCCESSFULLY_PROCESSED));
		document.setProcessedAt(LocalDateTime.now());
        
        return document;
	}
}
