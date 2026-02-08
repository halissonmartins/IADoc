package com.halisson.iadoc_document.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import okhttp3.Headers;

class FileSystemStorageServiceTest {

    private FileSystemStorageService storageService;
    private Path tempDir;

    @BeforeEach
    void setUp(@TempDir Path tempDirectory) throws IOException {
        tempDir = tempDirectory;
        ReflectionTestUtils.setField(storageService = new FileSystemStorageService(), 
            "receivedDir", tempDir.toString());

        // Create a test file
        Path testFile = tempDir.resolve("test-document.pdf");
        Files.write(testFile, "test content".getBytes());
    }

    @Test
    void testLoadAsResource_FileExists_Success() {
        Resource resource = storageService.loadAsResource("test-document.pdf");

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void testLoadAsResource_FileNotExists_ShouldThrowException() {
        assertThrows(RuntimeException.class, 
            () -> storageService.loadAsResource("non-existent.pdf"));
    }
}

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioStorageService storageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(storageService, "bucketName", "test-bucket");
    }

    @Test
    void testLoadAsResource_Success() throws Exception {

    	// 1. Seus dados em memória
    	ByteArrayInputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        
        // 2. Criar o GetObjectResponse manualmente
        // Assinatura: GetObjectResponse(Headers headers, String bucket, String region, String object, InputStream body)
        GetObjectResponse response = new GetObjectResponse(
            Headers.of("Content-Type", "application/octet-stream"), // Headers (okhttp3)
            "meu-bucket",        // Nome do bucket
            "us-east-1",         // Região
            "arquivo.txt",       // Nome do objeto
            inputStream                 // O seu ByteArrayInputStream
        );
        
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        Resource resource = storageService.loadAsResource("test-document.pdf");

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void testLoadAsResource_MinioException_ShouldThrowRuntimeException() throws Exception {
        when(minioClient.getObject(any(GetObjectArgs.class)))
            .thenThrow(new RuntimeException("Minio error"));

        assertThrows(RuntimeException.class, 
            () -> storageService.loadAsResource("test-document.pdf"));
    }
}
