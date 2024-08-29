package com.halisson.iadoc_document.batch;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentItemProcessor implements ItemProcessor<Document, Document> {
	
	private final VectorStore vectorStore;
	private final PdfDocumentReaderConfig pdfConfig;
	private final String receivedDir;
	private final String processedDir;
		
	public DocumentItemProcessor(
			VectorStore vectorStore, 
			PdfDocumentReaderConfig pdfConfig, 
			String receivedDir,
			String processedDir) {
		super();
		this.vectorStore = vectorStore;
		this.pdfConfig = pdfConfig;
		this.receivedDir = receivedDir;
		this.processedDir = processedDir;
	}

	@Override
	public Document process(Document document) throws Exception{
		
		log.debug("Processing document {}", document.getName());
		
		var documentName = document.getName()+"."+document.getDocumentType();
		var source = Paths.get(receivedDir).resolve(documentName);
				
		var pdfReader = new PagePdfDocumentReader(loadAsResource(source), pdfConfig);
		var textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        
        var target = Paths.get(processedDir).resolve(documentName);
        Files.move(source, target);
        
        log.debug("Finished processing document {}", documentName);
        
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.SUCCESSFULLY_PROCESSED));
		document.setProcessedAt(LocalDateTime.now());
        
        return document;
	}
	
	private Resource loadAsResource(Path file) {
		try {
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new RuntimeException("Could not read file: " + file.getFileName());

			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not read file: " + file.getFileName(), e);
		}
	}

}
