package com.halisson.iadoc_application.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

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

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootReceivedDir, 1)
					.filter(path -> !path.equals(this.rootReceivedDir))
					.map(path -> this.rootReceivedDir.relativize(path));
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootReceivedDir.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootReceivedDir.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(rootReceivedDir);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
