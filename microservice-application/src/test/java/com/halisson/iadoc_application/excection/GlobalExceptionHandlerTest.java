package com.halisson.iadoc_application.excection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleStorageFileNotFound() {
        StorageFileNotFoundException exception = new StorageFileNotFoundException("File not found");

        String result = exceptionHandler.handleStorageFileNotFound(exception);

        assertEquals("File not found", result);
    }

    @Test
    void testHandleNotFound() {
        NotFoundException exception = new NotFoundException("Resource not found");

        String result = exceptionHandler.handleNotFound(exception);

        assertEquals("Resource not found", result);
    }

    @Test
    void testHandleAlreadyExists() {
        AlreadyExistsException exception = new AlreadyExistsException("Resource already exists");

        String result = exceptionHandler.handleAlreadyExists(exception);

        assertEquals("Resource already exists", result);
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Internal error");

        String result = exceptionHandler.handleRuntimeException(exception);

        assertEquals("Internal error", result);
    }

    @Test
    void testStorageFileNotFoundException_WithCause() {
        Exception cause = new Exception("Original cause");
        StorageFileNotFoundException exception = new StorageFileNotFoundException("File error", cause);

        assertEquals("File error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testNotFoundException_WithCause() {
        Exception cause = new Exception("Original cause");
        NotFoundException exception = new NotFoundException("Not found error", cause);

        assertEquals("Not found error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testAlreadyExistsException_WithCause() {
        Exception cause = new Exception("Original cause");
        AlreadyExistsException exception = new AlreadyExistsException("Already exists error", cause);

        assertEquals("Already exists error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testStorageException_WithMessage() {
        StorageException exception = new StorageException("Storage error");

        assertEquals("Storage error", exception.getMessage());
    }

    @Test
    void testStorageException_WithCause() {
        Exception cause = new Exception("Original cause");
        StorageException exception = new StorageException("Storage error", cause);

        assertEquals("Storage error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
