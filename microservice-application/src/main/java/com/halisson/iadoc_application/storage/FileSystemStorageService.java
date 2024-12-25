package com.halisson.iadoc_application.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.halisson.iadoc_application.excection.StorageException;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootReceivedDir;

	public FileSystemStorageService(@Value("${UPLOAD_RECEIVED_DIR}")String receivedDir) {

        if(receivedDir.trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootReceivedDir = Paths.get(receivedDir);
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), this.rootReceivedDir.resolve(file.getOriginalFilename()));
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}
	
	@Override
	public void storeBatch(MultipartFile[] files) {
		
		Arrays.stream(files).forEach(this::store);

	}

}
