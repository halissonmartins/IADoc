package com.halisson.iadoc_document.storage;

import org.springframework.core.io.Resource;

public interface IStorageService {

	Resource loadAsResource(String documentName);
	
}
