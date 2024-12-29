package com.halisson.iadoc_document.storage;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;

@Service("minioStorageService")
@RequiredArgsConstructor
public class MinioStorageService implements IStorageService {
	
	private final MinioClient minioClient;	

    @Value("${minio.bucket.name}")
    private String bucketName;

	@Override
	public Resource loadAsResource(String documentName) {
				
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
