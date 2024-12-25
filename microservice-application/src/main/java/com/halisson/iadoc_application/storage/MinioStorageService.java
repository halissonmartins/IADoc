package com.halisson.iadoc_application.storage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageService implements StorageService {
	
	private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

	@Override
	public void store(MultipartFile file) {
		
        String fileName = file.getOriginalFilename();
        
        try (InputStream is = file.getInputStream()) {
        	
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName).stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            log.debug("Loaded file: " + bucketName + "/" + fileName);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image file.", e);
        }
	}

	@Override
	public void storeBatch(MultipartFile[] files) {
		Arrays.stream(files).forEach(this::store);
	}

}
