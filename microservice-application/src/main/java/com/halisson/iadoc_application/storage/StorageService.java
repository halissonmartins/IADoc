package com.halisson.iadoc_application.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void store(MultipartFile file);
	
	void storeBatch(MultipartFile[] files);

}
