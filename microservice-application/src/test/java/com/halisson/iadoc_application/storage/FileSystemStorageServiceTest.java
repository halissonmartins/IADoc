package com.halisson.iadoc_application.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import com.halisson.iadoc_application.excection.StorageException;

class FileSystemStorageServiceTest {

    private FileSystemStorageService storageService;
    private Path tempDir;

    @BeforeEach
    void setUp(@TempDir Path tempDirectory) {
        tempDir = tempDirectory;
        storageService = new FileSystemStorageService(tempDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up files created during tests
        Files.walk(tempDir)
            .filter(Files::isRegularFile)
            .forEach(file -> {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException e) {
                    // Ignore cleanup errors
                }
            });
    }

    @Test
    void testConstructor_WithEmptyPath_ShouldThrowException() {
        assertThrows(StorageException.class, () -> new FileSystemStorageService(""));
    }

    @Test
    void testConstructor_WithWhitespacePath_ShouldThrowException() {
        assertThrows(StorageException.class, () -> new FileSystemStorageService("   "));
    }

    @Test
    void testStore_ValidFile_Success() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        assertDoesNotThrow(() -> storageService.store(file));

        Path storedFile = tempDir.resolve("test.pdf");
        assertTrue(Files.exists(storedFile));
    }

    @Test
    void testStore_EmptyFile_ShouldThrowException() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(StorageException.class, () -> storageService.store(file));
    }

    @Test
    void testStoreBatch_MultipleFiles_Success() {
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

        assertDoesNotThrow(() -> storageService.storeBatch(new MockMultipartFile[]{file1, file2}));

        assertTrue(Files.exists(tempDir.resolve("test1.pdf")));
        assertTrue(Files.exists(tempDir.resolve("test2.pdf")));
    }
}
