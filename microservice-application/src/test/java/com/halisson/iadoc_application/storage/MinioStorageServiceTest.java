package com.halisson.iadoc_application.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioStorageService minioStorageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(minioStorageService, "bucketName", "test-bucket");
    }

    @Test
    void testStore_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        assertDoesNotThrow(() -> minioStorageService.store(file));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testStore_MinioException_ShouldThrowRuntimeException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        doThrow(new RuntimeException("Minio error")).when(minioClient).putObject(any(PutObjectArgs.class));

        assertThrows(RuntimeException.class, () -> minioStorageService.store(file));
    }

    @Test
    void testStoreBatch_Success() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "test1.pdf",
                "application/pdf",
                "test content 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "test2.pdf",
                "application/pdf",
                "test content 2".getBytes()
        );

        assertDoesNotThrow(() -> minioStorageService.storeBatch(new MockMultipartFile[]{file1, file2}));
        verify(minioClient, times(2)).putObject(any(PutObjectArgs.class));
    }
}
