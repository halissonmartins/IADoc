package com.halisson.iadoc_document.storage;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service("fileSystemStorageService")
@RequiredArgsConstructor
public class FileSystemStorageService implements IStorageService {
	
	@Value("${UPLOAD_RECEIVED_DIR}")
	private String receivedDir;

	@Override
	public Resource loadAsResource(String documentName) {
		
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

}
