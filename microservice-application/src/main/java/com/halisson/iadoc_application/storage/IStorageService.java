package com.halisson.iadoc_application.storage;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

	void store(MultipartFile file);
	
	void storeBatch(MultipartFile[] files);

}
