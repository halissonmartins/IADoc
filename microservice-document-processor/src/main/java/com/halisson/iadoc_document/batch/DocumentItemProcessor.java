package com.halisson.iadoc_document.batch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.halisson.iadoc_document.entity.Document;
import com.halisson.iadoc_document.entity.DocumentStatus;
import com.halisson.iadoc_library.enums.EnumDocumentStatus;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentItemProcessor implements ItemProcessor<Document, Document> {
	
	private final VectorStore vectorStore;
	private final PdfDocumentReaderConfig pdfConfig;
	private final String receivedDir;
	private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;
		
	public DocumentItemProcessor(
			VectorStore vectorStore, 
			PdfDocumentReaderConfig pdfConfig, 
			String receivedDir,
			MinioClient minioClient) {
		super();
		this.vectorStore = vectorStore;
		this.pdfConfig = pdfConfig;
		this.receivedDir = receivedDir;
		this.minioClient = minioClient;
	}

	@Override
	public Document process(Document document) throws Exception{
		
		log.debug("Processing document {}", document.getName());
		
		var documentName = document.getName()+"."+document.getDocumentType();		
				
		//var pdfReader = new PagePdfDocumentReader(loadAsResource(documentName), pdfConfig);
		var pdfReader = new PagePdfDocumentReader(loadAsInputStreamResource(documentName), pdfConfig);
		var textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        
        log.debug("Finished processing document {}", documentName);
        
        document.setDocumentStatus(new DocumentStatus(EnumDocumentStatus.SUCCESSFULLY_PROCESSED));
		document.setProcessedAt(LocalDateTime.now());
        
        return document;
	}
	
	private Resource loadAsResource(String documentName) {
		
		var source = Paths.get(receivedDir).resolve(documentName);
		
		try {
			
			Resource resource = new UrlResource(source.toUri());
			
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}else {
				throw new RuntimeException("Could not read file: " + source.getFileName());
			}
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not read file: " + source.getFileName(), e);
		}
	}
	
	private Resource loadAsInputStreamResource(String documentName) {
				
		// get object given the bucket and object name
		try {
			
			InputStream stream = minioClient.getObject(
					  GetObjectArgs.builder()
						  .bucket(bucketName)
						  .object(documentName)
						  .build());
			
			Resource resource = new InputStreamResource(stream);
			
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}else {
				throw new RuntimeException("Could not read file: " + documentName);
			}
			
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
				| IllegalArgumentException | IOException e) {
			throw new RuntimeException("Could not read file: " + documentName, e);
		}
	}

}
