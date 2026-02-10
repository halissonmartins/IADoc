package com.halisson.iadoc_application.excection; // Note the typo in package name 'excection' matching existing code

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ExceptionCoverageTest {

    @Test
    void testAlreadyExistsException() {
        AlreadyExistsException ex1 = new AlreadyExistsException("Message");
        assertNotNull(ex1);
        assertEquals("Message", ex1.getMessage());

        AlreadyExistsException ex2 = new AlreadyExistsException("Message", new RuntimeException());
        assertNotNull(ex2);
        assertEquals("Message", ex2.getMessage());
    }

    @Test
    void testNotFoundException() {
        NotFoundException ex1 = new NotFoundException("Message");
        assertNotNull(ex1);
        assertEquals("Message", ex1.getMessage());

        NotFoundException ex2 = new NotFoundException("Message", new RuntimeException());
        assertNotNull(ex2);
        assertEquals("Message", ex2.getMessage());
    }
}
